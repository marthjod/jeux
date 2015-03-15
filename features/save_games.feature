Feature: Save games

    Scenario Outline: Player 1 vs. player 2 (sets)
        When player <Player 1 ID> plays against player <Player 2 ID> in game <Game ID>
            | Score player 1 | Score player 2 |
            |             11 |              0 |
            |             11 |              0 |
            |              0 |              0 |

        Examples:
            | Player 1 ID | Player 2 ID | Game ID |
            |           1 |           2 |       1 |
