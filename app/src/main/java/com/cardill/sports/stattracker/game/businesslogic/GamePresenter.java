package com.cardill.sports.stattracker.game.businesslogic;

import com.cardill.sports.stattracker.common.data.Player;
import com.cardill.sports.stattracker.game.data.GameData;
import com.cardill.sports.stattracker.game.data.GameRepository;
import com.cardill.sports.stattracker.game.data.PendingStat;
import com.cardill.sports.stattracker.game.data.StatType;
import com.cardill.sports.stattracker.game.ui.GameViewBinder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class GamePresenter {
    private final GameViewBinder viewBinder;
    private final GameRepository gameRepository;
    private final GameViewModel viewModel;

    public GamePresenter(GameViewBinder viewBinder,
                         GameRepository gameRepository,
                         GameViewModel viewModel,
                         Observable<GameEvent> eventObservable) {
        this.viewBinder = viewBinder;
        this.gameRepository = gameRepository;
        this.viewModel = viewModel;

        init(viewModel, eventObservable);
    }

    private void init(GameViewModel viewModel, Observable<GameEvent> eventObservable) {
        Disposable subscribe = eventObservable
                .subscribe(gameEvent -> {
                    if (gameEvent instanceof GameEvent.MakeRequested) {
                        viewModel.setGameState(GameState.MAKE_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.PlayerSelected) {
                        GameEvent.PlayerSelected playerSelectedEvent = (GameEvent.PlayerSelected) gameEvent;

                        Player player = playerSelectedEvent.getPlayer();

                        viewModel.setCurrentTeam(playerSelectedEvent.getTeam());

                        GameState gameState = viewModel.getGameState().getValue();
                        if (gameState == null) return;

                        StatType statType = getStatKey(gameState);

                        if (gameState == GameState.ASSIST_REQUESTED
                                || gameState == GameState.REBOUND_REQUESTED
                                || gameState == GameState.REBOUND_FROM_BLOCK_REQUESTED
                                || gameState == GameState.BLOCK_REQUESTED
                                || gameState == GameState.STEAL_REQUESTED) {
                            if (gameState == GameState.ASSIST_REQUESTED) {
                                PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                                gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                                gameRepository.incrementStat(player.id(), statType);
                                viewModel.updateScore(gameRepository.getGameStats());
                                viewBinder.showStatConfirmation(player.firstName() + " assists to " + latestPendingStat.getPlayer().firstName());
                            } else if (gameState == GameState.REBOUND_REQUESTED) {
                                PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                                gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                                gameRepository.incrementStat(player.id(), statType);
                                viewBinder.showStatConfirmation(player.firstName() + " rebounded " + latestPendingStat.getPlayer().firstName() + "'s missed shot");
                            } else if (gameState == GameState.BLOCK_REQUESTED) {
                                gameRepository.incrementPendingStat(player, statType);
                                viewModel.setGameState(GameState.DETERMINE_BLOCK_EXTRAS);
                            } else if (gameState == GameState.STEAL_REQUESTED) {
                                PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                                gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                                gameRepository.incrementStat(player.id(), statType);
                                viewBinder.showStatConfirmation(player.firstName() + " stole from " + latestPendingStat.getPlayer().firstName());
                            } else if (gameState == GameState.REBOUND_FROM_BLOCK_REQUESTED) {
                                PendingStat miss = gameRepository.getLatestPendingStat();
                                PendingStat block = gameRepository.getLatestPendingStat();
                                gameRepository.incrementStat(miss.getPlayer().id(), miss.getStatType());
                                gameRepository.incrementStat(block.getPlayer().id(), block.getStatType());
                                gameRepository.incrementStat(player.id(), statType);
                                viewBinder.showStatConfirmation(miss.getPlayer().firstName() + " missed shot was blocked by "
                                        + block.getPlayer().firstName() + " and rebounded by "
                                        + player.firstName());
                            }

                            if (gameState != GameState.BLOCK_REQUESTED) {
                                viewModel.setGameState(GameState.MAIN);
                            }
                        } else if (gameState == GameState.MAKE_REQUESTED) {
                            gameRepository.incrementPendingStat(player, statType);
                            viewModel.setGameState(GameState.DETERMINE_MAKE_EXTRAS);
                        } else if (gameState == GameState.MISS_REQUESTED) {
                            gameRepository.incrementPendingStat(player, statType);
                            viewModel.setGameState(GameState.DETERMINE_MISS_EXTRAS);
                        } else if (gameState == GameState.TURNOVER_REQUESTED) {
                            gameRepository.incrementPendingStat(player, statType);
                            viewModel.setGameState(GameState.DETERMINE_TURNOVER_EXTRAS);
                        }
                    } else if (gameEvent instanceof GameEvent.AssistRequested) {
                        viewModel.setGameState(GameState.ASSIST_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.NoAssistRequested) {
                        PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                        gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                        viewBinder.showStatConfirmation(latestPendingStat.getPlayer().firstName() + " made shot");
                        viewModel.updateScore(gameRepository.getGameStats());
                        viewModel.setGameState(GameState.MAIN);
                    } else if (gameEvent instanceof GameEvent.MissRequested) {
                        viewModel.setGameState(GameState.MISS_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.BlockRequested) {
                        viewModel.setGameState(GameState.BLOCK_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.ReboundFromBlockRequested) {
                        viewModel.setGameState(GameState.REBOUND_FROM_BLOCK_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.NoReboundFromBlockRequested) {
                        PendingStat miss = gameRepository.getLatestPendingStat();
                        PendingStat block = gameRepository.getLatestPendingStat();
                        gameRepository.incrementStat(miss.getPlayer().id(), miss.getStatType());
                        gameRepository.incrementStat(block.getPlayer().id(), block.getStatType());
                        viewBinder.showStatConfirmation(block.getPlayer().firstName() + " blocked " + miss.getPlayer().firstName());
                        viewModel.setGameState(GameState.MAIN);
                    } else if (gameEvent instanceof GameEvent.ReboundRequested) {
                        viewModel.setGameState(GameState.REBOUND_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.NeitherRequested) {
                        PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                        gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                        viewBinder.showStatConfirmation(latestPendingStat.getPlayer().firstName() + " missed shot");
                        viewModel.setGameState(GameState.MAIN);
                    } else if (gameEvent instanceof GameEvent.TurnoverRequested) {
                        viewModel.setGameState(GameState.TURNOVER_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.StealRequested) {
                        viewModel.setGameState(GameState.STEAL_REQUESTED);
                    } else if (gameEvent instanceof GameEvent.NoStealRequested) {

                        PendingStat latestPendingStat = gameRepository.getLatestPendingStat();
                        gameRepository.incrementStat(latestPendingStat.getPlayer().id(), latestPendingStat.getStatType());
                        viewBinder.showStatConfirmation(latestPendingStat.getPlayer().firstName() + " turnover");

                        viewModel.setGameState(GameState.MAIN);
                    } else if (gameEvent instanceof GameEvent.BackRequested) {
                        if (viewModel.getGameState().getValue() == GameState.MAKE_REQUESTED) {
                            viewModel.setGameState(GameState.MAIN);
                        } else if (viewModel.getGameState().getValue() == GameState.DETERMINE_MAKE_EXTRAS) {
                            gameRepository.removeFromQueue();
                            viewModel.setGameState(GameState.MAKE_REQUESTED);
                        } else if (viewModel.getGameState().getValue() == GameState.ASSIST_REQUESTED) {
                            viewModel.setGameState(GameState.DETERMINE_MAKE_EXTRAS);
                        } else if (viewModel.getGameState().getValue() == GameState.MISS_REQUESTED) {
                            viewModel.setGameState(GameState.MAIN);
                        } else if (viewModel.getGameState().getValue() == GameState.DETERMINE_MISS_EXTRAS) {
                            gameRepository.removeFromQueue();
                            viewModel.setGameState(GameState.MISS_REQUESTED);
                        } else if (viewModel.getGameState().getValue() == GameState.REBOUND_REQUESTED) {
                            viewModel.setGameState(GameState.DETERMINE_MISS_EXTRAS);
                        } else if (viewModel.getGameState().getValue() == GameState.BLOCK_REQUESTED) {
                            viewModel.setGameState(GameState.DETERMINE_MISS_EXTRAS);
                        } else if (viewModel.getGameState().getValue() == GameState.TURNOVER_REQUESTED) {
                            viewModel.setGameState(GameState.MAIN);
                        } else if (viewModel.getGameState().getValue() == GameState.DETERMINE_TURNOVER_EXTRAS) {
                            gameRepository.removeFromQueue();
                            viewModel.setGameState(GameState.TURNOVER_REQUESTED);
                        } else if (viewModel.getGameState().getValue() == GameState.STEAL_REQUESTED) {
                            viewModel.setGameState(GameState.DETERMINE_TURNOVER_EXTRAS);
                        } else if (viewModel.getGameState().getValue() == GameState.MAIN) {
                            viewBinder.showExitConfirmation();
                        } else if (viewModel.getGameState().getValue() == GameState.DETERMINE_BLOCK_EXTRAS) {
                            gameRepository.removeFromQueue();
                            viewModel.setCurrentTeam(viewModel.getCurrentTeam() == Team.TEAM_ONE ? Team.TEAM_TWO : Team.TEAM_ONE);
                            viewModel.setGameState(GameState.BLOCK_REQUESTED);
                        }
                    }
                });
    }

    private StatType getStatKey(GameState value) {
        switch (value) {
            case ASSIST_REQUESTED:
                return StatType.AST;
            case MAKE_REQUESTED:
                return StatType.FGM;
            case MISS_REQUESTED:
                return StatType.MISSES;
            case BLOCK_REQUESTED:
                return StatType.BLK;
            case STEAL_REQUESTED:
                return StatType.STL;
            case REBOUND_REQUESTED:
                return StatType.REB;
            case TURNOVER_REQUESTED:
                return StatType.TO;
            case REBOUND_FROM_BLOCK_REQUESTED:
                return StatType.REB;
        }

        throw new IllegalStateException("Invalid game state");
    }

    public void submitGameStats() {
        GameData gameData = gameRepository.getGameStats();
        viewModel.saveGame(gameData);
        boxScoreRequested();
    }

    public void detailsRequested() {
        viewBinder.showDetails();
    }

    private void boxScoreRequested() {
        viewBinder.showBoxScore();
    }

    public void saveGameRequested() {
        viewBinder.showGameOverConfirmation();
    }
}