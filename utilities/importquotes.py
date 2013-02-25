#!/usr/bin/python


import slickqa
import sys

def main(quotefile, slickurl):
    with open(quotefile, 'r') as quotes:
        slick = slickqa.SlickConnection(slickurl)
        for quote in quotes:
            chuckquote = slickqa.Quote()
            chuckquote.imageUrl = "https://twimg0-a.akamaihd.net/profile_images/1697024721/Chuck_Norris.jpg"
            chuckquote.quote = quote.rstrip()
            slick.quotes(chuckquote).create()

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("You must provide the slick url as the parameter")
        sys.exit(1)
    main("chucknorrisquotes.txt", sys.argv[1])
