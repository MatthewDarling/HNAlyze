# hnalyze

I recently read what I thought was a pretty thought-provoking Hacker News story, but found the comments were really terrible, and in part because of that it disappeared quickly off the front page. So I wanted to put some 

## Installation

Git clone and then run `lein repl`.

## Usage

Start with the `get-comments` function, which takes a story id, and then `parse-api-json` on the return value of that. `def` the return value of that function, because you'll be using it for all the others.

For now the only interface is the REPL, but I might add some command line functionality or learn enough ClojureScript to put it on Heroku. It would be interesting to have sort of an interactive thing, where you start by providing a story ID and then get a bunch of info about it. That's pretty much what the REPL provides, though...

## Examples

...

## License

Copyright © 2014 Matthew Darling

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
