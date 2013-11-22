## /rest/v1/create-roundswitchrule

```bash
curl -X PUT -H "Content-Type: application/json" \
-d '{"srcGroupId":1, "destGroupId":4, "startWithRank":-1,"additionalPlayers":0}' \
http://localhost:8080/JeuxWeb/rest/v1/create-roundswitchrule
```

WARN  [de.fhb.jeux.session.CreateRoundSwitchRuleBean] (http--0.0.0.0-8080-6) Must start at least at rank 1.

```bash
curl -X PUT -H "Content-Type: application/json" \
-d '{"srcGroupId":2, "destGroupId":4, "startWithRank":1,"additionalPlayers":0}' \
http://localhost:8080/JeuxWeb/rest/v1/create-roundswitchrule
```

ERROR [de.fhb.jeux.dao.GroupDAO] (http--0.0.0.0-8080-6) Group ID 2: No entity found for query

ERROR [de.fhb.jeux.session.CreateRoundSwitchRuleBean] (http--0.0.0.0-8080-6) Source or destination group is null.

```bash
curl -X PUT -H "Content-Type: application/json" \
-d '{"srcGroupId":1, "destGroupId":4, "startWithRank":1,"additionalPlayers":3}' \
http://localhost:8080/JeuxWeb/rest/v1/create-roundswitchrule
```

WARN  [de.fhb.jeux.session.CreateRoundSwitchRuleBean] (http--0.0.0.0-8080-1) Too many players to be moved (last considered rank 4 > source group size of 2).

```bash
curl -X PUT -H "Content-Type: application/json" \
-d '{"srcGroupId":1, "destGroupId":4, "startWithRank":4,"additionalPlayers":3}' \
http://localhost:8080/JeuxWeb/rest/v1/create-roundswitchrule
```

WARN  [de.fhb.jeux.session.CreateRoundSwitchRuleBean] (http--0.0.0.0-8080-1) Rank exceeds group size.
