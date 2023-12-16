#### Name : Tudor Cristian-Andrei
#### Group : 321CAa

> I used my code from previous stage.

# GlobalWaves - Part2

## Overview
Because I used a **Command Pattern** from the beginning of the first stage, it was
easy for me to add new functionalities. The program acts the same, it is centered around
one big **ActionManager**, that controls the flow of multiple request from commands, one
by one.
> ### Recap: 
> There is this Library, usually referenced in the program as **database**, that uses
> the **Singleton Pattern**, so every **Admin** has access to the same datas. Those are new
> entities, **Admin**, and **AdminBot** (old LibraryInterrogator). They're the ones that are capable
> of read and write to the database (for now just the AdminBot has the right to read). Because it makes
> more sense, the **ActionManager** is now a Singleton, too. The manager get requests from the **Interrogators**,
> and it has to answer them, one way or another. It uses an AdminBot to answer those requests, and the
> **HelperTool** class was designed to contain multipurpose functions used to parse results.

### What's new ?
> #### Pages:
> There were added pages. In my version of the app, a **Page** has only "the power" to
> contain something. For printing the page contents, I used the **Visitor Pattern**. Each page is printed different,
> but is visited by the same ContentVisitor.
> 
> A Page is integrated in a User, and a User is integrated in a Page (for artists and hosts). They can't exist one
> without the other. This ensures that there is no artist or host without page.
>
> The CurrentPage of a user is just a reference to the page. The pages are never copied, just referenced.
> 
> For the HomePage and the LikedContent page, they don't have 2 lists, they have 2 **references** to the lists within the
> user object. When the user likes or follow something, the changes are visible instantaneously, and the pages don't
> need to be updated for each user.
> 
>> It was easy to integrate a new type of search, because of the SearchEngines. The hardest part was to modify the 
>> SearchBar to accept Pages. I integrated a Pager, that searches only pages. The search command uses either the engine
>> or the pager. This is how it figures out what to select.
>> Anyway, I had to modify the restriction on  type of search engine, now accepts any type, not just objects that
>> extends PlayableEntity.

> #### Artists and Hosts:
> Artist, Hosts and normal Users, they act the same. I used **polymorphism** to make a user act different in different
> contexts.

> #### Events, Merch, Announcement
> They're simple "graphical" features, easy to implement. They could have been extended from a base class,
> but I wanted them to be separate.

> #### Albums:
> Albums acts as playlists, with some additions, and separate place in the Library.
> > Structure of the Library : 
> > * _songs_ --> the songs that are being loaded at the very beginning
> > * _podcasts_ --> the podcasts that are being loaded at the very beginning
> > * _addedSongs_ --> the songs added implicit by adding an album. They're separated in a Map,
> > so it reduces the number of searches when it has to delete an album
> > * _addedPodcasts_ --> the podcasts that are added by the hosts. They're structured in the same way
> > the addedSongs are structured, to reduce the number of searches

### Modifications: 
The Player and the Playlists suffered severely modifications. In the past, playlists used just one list of
shuffling order, which was a big issue, because multiple users can listen to the same playlist at the same time.
The entire mechanism for next and prev was also changed. It's much simpler now, easy to understand.

Each player now uses its own shuffling order, so the users doesn't share order of playing.

## Improvements
* Coding style, obviously
* Documentation ( *I'll fix it for the next stage* )
* A Factory Pattern for Search Engines, but I didn't know about Factory at that time
* Maybe some refactoring

## Chat-GPT Suggestions
* I used Chat-GPT to find out how to use the LocalDate class, why Date has method deprecated, why it
makes  Feb 28 from Feb 31 (that's why there's an extra checking there). I wanted faster solutions to
parse the date.