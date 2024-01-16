### Name: Tudor Cristian-Andrei
### Group: 321CAa

> I used my code from **stage one** and **stage two**.

# GlobalWaves - Part3

## <font color="#5F8670"> Recap </font>
The implementation for GlobalWaves is centered around two main entities, <font color="red">Library</font> and
<font color="red">ActionManager</font>. The ActionManager manages the requests and reads / writes to Library using
an <font color="red">Accessor</font>. The Accessor is like a more powerful <font color="red">Admin</font>. When I
designed the app, I made it like this, so multiple admins can exist at one time, but I wanted them to be limited.

The <font color="red">Library</font> is a **Singleton**, which stores all the data: <font color="red">Users, Artists,
Hosts, Playlists, Songs, Podcasts</font>. The data inside the Library is very structured:
* Playlists are stored in a List inside a Map, by the user they belong to.
* Songs are stored in a List inside a Map, by the artist they belong to, but they're not grouped by albums. 
* Podcasts are stored in a List inside a Map, by the host they belong to.
* The users, the artists and the hosts are stored separately.

This convention reduces the time for searching, but makes the code difficult to read, and in some cases it is harder to
remove something from the Library.

Another **Singleton** I used is the <font color="red">ActionManager</font>. It is the central part of the app, because
everything has to pass through the manager. It acts like a filter for each command, which is performed only if it
approves. The manager makes every change, because it is the only one who has the access to the players, because it
stores them. When a new user is added it automatically creates a new <font color="red">UserInterface</font>.

<font color="red">UserInterfaces</font> were introduced since 2.0, and they encapsulate the user runtime information.
It stores the player, the searchbar, some data about pages navigation and a "link" to the user.
> #### Observation - "Links":
> Most of the time, there are no clones of the objects. Every data structure that uses an object uses its reference.
> They act like a "symbolic link" to the actual object. For example, the **User** from the **UserInterface** is the same
> everywhere. Using this approach, every change made to the object is visible globally.
> 
> The link "type" of field can be easily identified in the songs, albums and episodes structure, introduced since 3.0.

> **Note** : I'll start describing what's new on this version, but I'll return and explain some of the details about
> the app overall.

## <font color="#5F8670"> What's modified? </font>
* The identifiers: Since 3.0, a timestamp was assigned to each entity when it was created. From now on, a unique id is
assigned to the entity (this change happened because the order of creation matters, not the timestamp. They can be
created at the same time, but in a different order)
* Playlist and Album Constructors: Because there were too many parameters to the constructor, I added a **Builder**
pattern, instead of a classic constructor.
* Pagination system: The current page is now a part of the <font color="red">PageHistory</font> class, and the
**UserInterface** encapsulates this history.
* The **HomePage** contains extra information, so its **visit** method was modified. I was inspired by the official
solution to change the printing and use **Streams** instead of **StringBuilder**.
* The **getName** and **getIdentificationNumber** from <font color="red">PlayableEntity</font> interface were removed
and split into two separate **functional interfaces**, <font color="red">NamePossessor</font> and
<font color="red">UniqueIdPossessor</font>. They act like a property of an object. The property to have a name and the
one to have an id. I split the method in order to do something more generic, to avoid duplicating code.


## <font color="#5F8670"> Improvements </font>
* (<font color="FF9800">enhancement</font>) Because there were introduced IDs, the **creationTime** remains unused,
and it should be removed. <font color="C3E2C2"> - open issue</font>
* (<font color="FF9800">enhancement</font>) As **Florian-Luis Micu** suggested in **stage 2 feedback**, the exit-code
enums should be moved as an inner class of the **interrogators**. I started doing this after the feedback, but there
would have been a lot of work to move all of them. <font color="C3E2C2"> - open issue</font>



