package com.cardill.sports.stattracker.consumer.common.data;

import com.cardill.sports.stattracker.consumer.common.data.Game;
import com.cardill.sports.stattracker.consumer.common.data.GameDay;

/**
 * User triggered events.
 */
public interface ScoreEvent {
    class DateSelected implements ScoreEvent{
        private final GameDay gameDay;

        public DateSelected(GameDay gameDay) {
            this.gameDay = gameDay;
        }

        public GameDay getGameDay() {
            return gameDay;
        }
    }

    class GameSelected {
        private Game gameDay;

        public GameSelected(Game gameDay) {
            this.gameDay = gameDay;
        }

        public Game getGameDay() {
            return gameDay;
        }
    }
}
