package org.games.gameofthree;

import org.games.gameofthree.domain.models.Game;
import org.games.gameofthree.domain.models.Move;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.games.gameofthree.commons.Constants.PLAYER_1;
import static org.games.gameofthree.commons.Constants.PLAYER_2;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameOfThreeIntegrationTest {

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testGameOfThree() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();


        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(StompSession stompSession,
                                       StompHeaders stompHeaders) {

                stompSession.subscribe("/topic/game", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return Game.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object obj) {
                        try {
                            Game game = (Game) obj;
                            assertThat(game.getMoves()).isNotNull();
                            assertThat(game.getMoves().size()).isEqualTo(3);

                            Move firstMove = game.getMoves().get(0);
                            assertThat(firstMove.getPlayer().getName()).isEqualTo(PLAYER_1);
                            assertThat(firstMove.getAdditive()).isEqualTo(0);
                            assertThat(firstMove.getOutput()).isEqualTo(11);

                            Move secondMove = game.getMoves().get(1);
                            assertThat(secondMove.getPlayer().getName()).isEqualTo(PLAYER_2);
                            assertThat(secondMove.getAdditive()).isEqualTo(1);
                            assertThat(secondMove.getOutput()).isEqualTo(4);

                            Move lastMove = game.getMoves().get(2);
                            assertThat(lastMove.getPlayer().getName()).isEqualTo(PLAYER_1);
                            assertThat(lastMove.getAdditive()).isEqualTo(-1);
                            assertThat(lastMove.getOutput()).isEqualTo(1);

                            assertThat(game.getWinner().getName()).isEqualTo(lastMove.getPlayer().getName());

                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            stompSession.disconnect();
                            countDownLatch.countDown();
                        }
                    }
                });
                try {
                    stompSession.send("/app/play", "11");
                } catch (Throwable t) {
                    failure.set(t);
                    countDownLatch.countDown();
                }

            }

        };
            this.stompClient.connect("ws://localhost:{port}/game-web-socket", this.headers, handler, port);

        if (countDownLatch.await(10, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        } else {
            fail("Game of Three of three not started");
        }
    }

}
