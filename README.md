**Name : Tudor Cristian-Andrei**

**Group : 321CAa**

## <font color="#C683D7"> GlobalWaves - Part1 </font>

### Flow of the program

#### Library 
> My implementation is focused on an object named <font color="#7071E8">ActionManager</font>. It acts like a translator 
> between the JSON input, that is supposed to be an action performed by a user (since the name, ActionManager, duh...),
> and the <font color="#7071E8">Library</font> or the players. More than that, the <font color="#7071E8">ActionManager
> </font> doesn't have the **power** to make directly changes or directly interrogate it, to retrieve data, but it have to 
> use a <font color="#7071E8">LibraryInterrogator</font>. This may seem a little bit out of scope, or nonsense, but I
> wanted the <font color="#7071E8">Library</font> to act as a database, that does not have **powers**.
> > Disclaimer : I'm going to use a lot the word <font color="AED2FF">power</font> in this README, as a "metaphor" for
> methods.
>
> The only **powers** *( do you see ? )* of the <font color="#7071E8">Library</font> is to store, to accommodate new
> entities, or get rid of them, and to load the data (and maybe reset it, if I can say so, in some cases : when action is
> called again, if the users were loaded, it resets the list that contains the liked songs). Because it's a common database,
> for <font color="#7071E8">Library</font> I used the **Singleton Design Pattern**.
> 
 #### ActionManager
> Let's talk about the <font color="#7071E8">ActionManager</font>. It have some **powers**, but they're limited. In the
> first place, it's scope was to respond to requests. It also stores action-related data: a Map with all players, a
> SearchBar, that is common for all users, and the time when last action happened. It also uses a <font color="#7071E8">
> LibraryInterrogator</font> when it needs to retrieve data or send data to the library.
> 
> Who sends requests to the <font color="#7071E8">ActionManager</font> ? Answer : An <font color="#7071E8">Interrogator
> </font>. All commands acts as interrogators for the <font color="#7071E8">ActionManager</font>, because they'll always
> want something from it, in general exit codes, or exit messages. The interrogators sends requests to the Manager, and
> it will respond to those requests. A conversation between those 2 will sounds like this :
> 
> > <font color="FDF4F5">Interrogator</font> : Hey bro, can you follow this source for me ?
> 
> > <font color="E8A0BF">ActionManager</font> : Yes, bro, give me one second. I'll be back.
> 
> <font color="009FBD">A few moments later...</font>
> 
> > <font color="E8A0BF">ActionManager</font> : Sorry bro, but you want to follow a song, and this is not possible.
> 
> > <font color="FDF4F5">Interrogator</font> : Don't worry, bro, it's fine. But now I want something else from you. This
> is harder. I need the Top Five Most Liked Songs.
> 
> > <font color="E8A0BF">ActionManager</font> : I think I know a man that can help you ... 
>
> <font color="009FBD">A few moments later...</font>
> 
> > <font color="E8A0BF">ActionManager</font> : Look, my man gave me this.
> 

#### Interrogators
> For implementing the commands I used *something like* a Command Pattern (I don't know very much about the Command
> Pattern at this time, but it seems similar with the examples from web, more or less). Basically, each command calls execute,
> inherited from the *CommandObject* and *overridden*, one of the OOP concepts that I used very often being the **dynamic polymorphism**.
> It executes the command and generates an output JsonNode. By executing, I mean that it sends the request to the manager, and
> parse it's return exit code.


#### SearchEngine
> The search command uses a specific **Search Engine**, based on the type of searching, so there are 3 types of search engines:
> **SongEngine**, **PodcastEngine**, and **PlaylistEngine**. The engine uses <font color="#7071E8">Filters</font>. It may
> seem a little inappropriate to use generics for this, but I wanted to reuse the **NameFilter**, and the **OwnerFilter**. Something
> important about those engines is the fact that they cannot store results. They're designed just for parsing filters and searching, not storing.
> Of course that a field wouldn't have influenced too much, but it was my way of thinking, the logic behind the classes, that I
> wanted to respect. It doesn't even know how to parse the results. For that, I created a general purpose class (but I didn't need
> it after that).
> 
> The search results are stored by the SearchBar. This is it's scope.
> > Disclaimer : I would have done search different now, but when I started working I didn't have too many ideas.
> 

#### PlayableEntity and OwnedEntity

- OwnedEntity has a single propriety, to be owned by someone, something, so it can return an owner
- PlayableEntity : is the most important entity in the entire program. The PlayableEntity is the entity that the player accepts
for loading. There is a difference between PlayableEntity property, and the entities that are played. **The PlayableEntity can
be loaded into the player** and **The AudioFile can be played by player**. The Song is the only one that acts as both.
  - The AudioFile have just one **power** : to be liked.


#### Player
> Each user has its player. The player knows at every time what source is loaded, what song is playing, what is before it,
> what is after it, about the shuffle option, the repeat state, how much time is remained from what is playing. There is
> one more. The **history** field. When there is a Podcast playing, if the playing file stops premature, the players saves
> its remained time, and put it in history. When it's loaded back, it is removed from history, and put back in the same 
> conditions if necessary. It can be easily **extended** to track history for playlists and songs.
> 
> At each timeskip, the players are updated. If something happens like repeat, or shuffle, the prev and next are always updated.


#### Other observations about the structure
- Likes : The User class is different from the UserInput class. It has a list that contains the liked songs for each user. Using 
this approach it was easier to access fast the liked songs, but slower to find a list with the public songs.
- Playlist are mapped : The playlists are mapped by username. I have to mention that it would have been better and more elegant
to map by User, not username, but I did it this way for skipping at most 3 lines of code :)). It is faster to get the
user playlists for showPlaylists command, but slower for Top5Playlists.
- Enums : Most of them are declared as inner types of a class,

> Disclaimer again : I'm really sorry about the Javadoc comments missing. I tried to add the most important ones after
> realising I don't have the time and *power* to fix 700 errors of coding style.


#### Used Links:
- https://www.tutorialspoint.com/jackson_annotations/jackson_annotations_jsontypeinfo.htm
- https://stackoverflow.com/questions/5455014/ignoring-new-fields-on-json-objects-using-jackson
- https://www.baeldung.com/java-jsonnode-get-keys
- https://www.geeksforgeeks.org/iterators-in-java/
- https://www.geeksforgeeks.org/wildcards-in-java/
- https://www.geeksforgeeks.org/generics-in-java/
- https://www.baeldung.com/java-jackson-remove-json-elements


