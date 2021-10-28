# Reinforcement Example

This is a very small visual example of Donald Michie's approach to reinforcement learning from back in the 60's.  The example trains an agent to play the game of Nim on 5 objects.

# Rules of Nim
There are 5 objects and any player can take any of 1,2 or 3 objects from left to right.  The goal is to be the player to take the last object.

# Learning

The states of the game are:<br /> 
xxxxx<br /> 
-xxxx<br /> 
--xxx<br /> 
---xx<br /> 
----x<br /> 
where x is the object and - is an object that has been taken.

There are 5 boxes, one for each state and there are dots placed in the boxes representing beads which are the actions the agent can take in that state.  In Donald Michies original experiment, colored beads were randomly drawn and the agent would take that action, affecting the world.  After the game is over, for each state the game was in the agent's actions are reinforced by either putting 2 colored beads matching the action taken in that state back into the box if the agent WON or by taking 1 of that color out if the agent lost.  

In doing this process n times, as n tends to infinity we see convergence to an optimal policy (if such a policy does exist).  In this case the optimal policy is to take 1 object in the first action and then take the difference of 4-q where q is the amount the opponent takes.  The agent will almost always learn this policy in under 100 trials.

# Using this

Run the code (Jar coming soon).  Press the "get action" button to recieve the agents action.  Use the arrows underneathe the "get action" button to select the number you'd like to take and then click "Take".  Repeat this until the game is over.  When the game is over each state that was reached will be highlighted in green and the ones that the agent made an action in will have a box with a number inside.  For each box with a these boxes with a number in it, select the radio button (Take x) that corresponds with this number and left click to add a bead to that box or right click to remove that bead.  It is suggested to Add 2 beads on success and remove 1 on failure but you are welcome to play around with these rewards as you please.  At any point you can click "Reset" to reinitialize the game.
