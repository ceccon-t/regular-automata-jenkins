# Project
Regular Automata is an application that allows the use of finite automata to express regular languages by checking if certain "words" belong to them. (More information about this area can be found [here](https://en.wikipedia.org/wiki/Automata_theory))

It was developed as the final project for a college class on formal languages at UFRGS (Universidade Federal do Rio Grande do Sul) and was later expanded to encompass the most basic functionality one would expect from these formalisms. It is not meant to be the most comprehensive nor the most efficient program to do that, but to be an example of a possible implementation of these concepts in a straightforward and hopefully understandable manner, using a language that allows the code to be very expressive - as well as a playground to implement certain non-trivial algorithms by hand. As such, the source code is left here in case it can help anyone who is studying this subject to have a better grasp of it, in an educational context.

## How to download

If you want to download the final application instead of the source code, the latest version can always be found [here](https://bitbucket.org/ceccon-t/regular-automata/downloads/RegularAutomata.jar).

## How to build
Since it is a [Maven](https://maven.apache.org/) project, assuming you have Maven installed it should be as simple as running `mvn clean install` on the root directory.

## How to run
After it has been built, a .jar with everything needed to run inside of it will be placed on the "target" directory. Just double clicking it or going to the root directory and running `java -jar ./target/RegularAutomata.jar` (on Linux, or its equivalent on Windows) will open the application.

## What is currently present
As it stands now, the application works with DFA, NFA and NFAe automata that can be loaded from correctly formatted files (more info below). It will always load the automaton as the most restrictive type it falls into between these three. It is then possible to convert to any other type. The user can either enter words manually or load a list of them from a file and run these words through the automaton, receiving as a result if the automaton recognizes or not each word.

## What is being developed
Nothing at the moment.

## What is not currently under consideration but would be cool to have
Display of properties about the automata, such as if its recognized language is finite or infinite, if it is a minimal automaton, so on.
A wizard-like interface to create automata in the application itself.

## File formats for automaton and list of words
There are some files on `examples` folder of the formats the application expects. 


For lists of words, the file should consist of words (generic strings containing only symbols from the alphabet specified to the automaton), each on its own line.


An automaton file should be in the following format, with no blank lines (and preferably with a `.atm` extension, though that is not a hard requirement):


`Name`=({`comma-separated alphabet`},{`comma-separated states`},`initial state`,{`comma-separated final states`})

Prog

(`state`,`symbol`)=`state` 		`(one or more lines defining the transition function, for epsilon transitions leave symbol empty)`


Example:


AUTOMATON=({a,b},{q0,q1,q2,q3},q0,{q1,q3})

Prog

(q0,a)=q1

(q0,b)=q2

(q1,b)=q2

(q2,a)=q3

(q2,a)=q2

(q3,a)=q3

(q3,)=q2

(q3,b)=q2
