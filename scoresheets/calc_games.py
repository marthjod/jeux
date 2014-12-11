#!/usr/bin/python

# calc_games.py
#
# Reads a list of player names from a file and pairs players off in 1-on-1 games,
# scheduling games in a fair order (i.e. same amount of "breakroom time"). 
#
# marthjod@gmail.com lastmod 2012-12-01


# To implement a queue, use collections.deque which was designed to have fast appends and pops from both ends
# http://docs.python.org/tutorial/datastructures.html

import sys
from collections import deque
from math import factorial

def usage():
    print sys.argv[0] + " <playernames file> [--sql <group_id>|--latex]"
    
# math functions from 
# http://stackoverflow.com/questions/2096573/counting-combinations-and-permutations-efficiently
def product(iterable):
    prod = 1
    for n in iterable:
        prod *= n
    return prod

# permutation
def npr(n, r):
    assert 0 <= r <= n
    return product(range(n - r + 1, n + 1))

# combination
def ncr(n, r):
    assert 0 <= r <= n
    if r > n // 2:
        r = n - r
    return npr(n, r) // factorial(r)
    
def main():
    # continue only if user provided a filename as arg
    if(len(sys.argv) < 2):
        usage()
    else:
        filename = sys.argv[1]
        with open(filename, 'r') as f:
            lines = f.readlines()
            
        # get rid of newlines in lines and fill players list
        players = []
        for line in lines:
            players.append(line.strip())
        
        # iterate over players to create games list (nested list)
        games = []
        
        # for players which have played only recently 
        # and should be ignored in the 1st-nth iteration (if possible)
        breakroom = deque()

        # do until all possible games are calculated
        max_games = ncr(len(players), 2)
        while len(games) < max_games:
        
            # outer loop
            for player1 in players: 
                # inner loop
                for player2 in players:
                    
                    # cannot play against oneself 
                    if player1 == player2: 
                        continue
                    
                    # game already in list?
                    if not([player1, player2] in games or [player2, player1] in games): 

                        if player1 not in breakroom and player2 not in breakroom:
                            # in this case, add them immediately to a game, 
                            # then to the breakroom
                            games.append([player1, player2])
                            breakroom.append(player1);
                            breakroom.append(player2);
                            
                        elif (player1 in breakroom and player2 not in breakroom):
                            # don't use current player2 for this loop any more
                            break
                        else:
                            # "FIFO" a breakroom player:
                            # the first one who came in becomes available again now
                            breakroom.popleft()

        print "-- %i games calculated (%i possible)" % (len(games), max_games)

        for game in games:
            if len(sys.argv) > 3:
                if sys.argv[2] == "--sql":
                    group_id = sys.argv[3];
                    print "-- %s vs. %s" % (game[0], game[1])
                    print "INSERT INTO game (player1_id, player2_id, group_id) VALUES ('%s', '%s', '%s');" % (game[0], game[1], group_id)
            elif len(sys.argv) > 2:
                if sys.argv[2] == "--latex":
                    with open('SCORESHEET_TEMPLATE_SINGLE.tex', 'r') as template:
                        # write LaTeX file scoresheet_player1-player2.tex
                        with open("scoresheet_%s-%s.tex" % (game[0], game[1]), 'a') as texfile:
                            while 1:
                                line = template.readline()
                                if not line:
                                    break
                                line = line.replace("PLAYER1NAME", game[0])
                                line = line.replace("PLAYER2NAME", game[1])
                                texfile.write("%s\n" % line)
            # neither SQL not LaTeX output
            else:
                print "%s vs. %s" % (game[0], game[1])

if __name__ == "__main__":
    main()

