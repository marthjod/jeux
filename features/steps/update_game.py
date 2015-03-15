step_matcher("re")

import requests
import json

from defaults import default_headers, rest_api_admin_url


@when(u'player (?P<player1_id>\d+) plays against player (?P<player2_id>\d+) in game (?P<game_id>\d+)')
def when_player1_vs_player2(context, player1_id, player2_id, game_id):
    url = rest_api_admin_url + "/update-game"

    sets = []
    for row in context.table:
        sets.append({
            "gameId": game_id,
            "player1Score": row['Score player 1'],
            "player2Score": row['Score player 2']
        })

    payload = {
        "id": game_id,
        "player1Id": player1_id,
        "player2Id": player2_id,
        "sets": sets
    }

    r = requests.post(url, data=json.dumps(payload), headers=default_headers)
    assert r.status_code == requests.codes.ok, "Got unexpected HTTP status code %d\nPayload: %s" % (r.status_code, json.dumps(payload))


