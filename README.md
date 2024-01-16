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
> the app overall if it is necessary.

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

## <font color="#5F8670"> What's new? </font>
> #### Wrap system
> **GlobalWaves 3.0** uses different HashMaps to track the activity for each user. Each User has to implement the
> **getStatistics** method. The output for this command is selected between multiple types of outputs:
> <font color="red">UserStatistics, ArtistStatistics, HostStatistics or NoDataStatistics</font>.
>
> Each Statistics class is an inheritance of the <font color="red">StatisticsTemplate</font> abstract class, so I
> considered appropriate to use a factory to reduce the number of possibles if statements.
> 
> Note that there is something that may seem unfamiliar: Why there is a <font color="red">Genre</font> class?
> Why do we need a genre class if it's just a String?
> 
> Answer: I wanted to use the generic function I made for all **NamePossessor**, so I needed a wrapper for String.

> #### Monetization system
> **GlobalWaves 3.0** uses two entities for this requirement: A HashMap which stores the songs already paid, and a
> <font color="red">MoneyTracker</font>, which clears the history and sends the money to the artists whenever is
> awakened to do a payment.
> 
> The MoneyTracker uses the song link to the artist to send the money.
> In order to track the song and to pay the song, a link to the user was also added in Player class.
> 
> The payment happens when an ad is played, not loaded!

> #### Notification system
> **GlobalWaves 3.0** uses an **Observer** pattern for notifications.
> 
> The Observer can be confusing at some points. It uses the generic **Observer** and **Observable** interfaces,
> which can be used for anything, within the limits of the defined methods.
> 
> The type **T** from **Observer** and **Observable** means what the objects observe.
> 
> <font color="red">The Inbox</font> is the specific **Observer** used, and <font color="red">The Notifier </font>
> is the **Observable** object. In our case, the **inbox** observes <font color="red">Notifications</font> and the
> notifier sends **Notifications**.
> 
> Each User has attached an Inbox, to observe notifications, and a Notifier, to send notifications. It works silently.
> They're shown only when the user requires.

> #### Recommendations system
> It uses a similar approach to the wrap system. There are three different classes inherited from
> <font color="red">Recommender</font>, one for each type of recommendation. There is also a factory, which generates
> a specific **Recommender** based on the type provided.
> 
> The recommendations are stored directly on the user's homepage.
> 
> **Remainder**: The User has a "link" to the page, and the page has a "link" to the user, so it doesn't matter where
> they're stored, because the user can access them at any time.

> #### Page Navigation system
> **GlobalWaves 3.0** uses the <font color="red">Page History</font> mentioned earlier, to store the pages.
>
> It uses two dequeues and a reference to the current page. It isn't mandatory to use two dequeues, but that's the way
> I wanted it to be. One queue for previous pages and one for the next pages. The current page remains outside until a
> new page is visited.
> 
> The prev pages are pushed from end to start, and the next pages from start to end.
> 
> But why?: If you put together the two arrays and the current page in the middle, it gives the precise order.

## <font color="#5F8670"> OOP Concepts Used </font>
* Encapsulation.
* Inheritance.
* Polymorphism.
* Abstraction.
* Design Patterns (new design patterns since 3.0: **Builder**, **Factory**, **Observer**).
* Not an OOP concept, but I have to mention Java Generics

## <font color="#5F8670"> Improvements </font>
* (<font color="FF9800">enhancement</font>) Because there were introduced IDs, the **creationTime** remains unused,
and it should be removed. <font color="C3E2C2"> - open issue</font>
* (<font color="FF9800">enhancement</font>) As **Florian-Luis Micu** suggested in **stage 2 feedback**, the exit-code
enums should be moved as an inner class of the **interrogators**. I started doing this after the feedback, but there
would have been a lot of work to move all of them. <font color="C3E2C2"> - open issue</font>
* (<font color="FF9800">enhancement</font>) The tests didn't cover all the possible cases, and there are still some
issues. For example, a song played / replayed after **prev** or **next** won't get paid. The same problem remains for **skip** and
**rewound**. <font color="C3E2C2"> - open issue</font>

## <font color="#5F8670"> ChatGPT Suggestions </font>
Our AI friend helped me to understand the Streams, before I learned from OOP Labs.
I asked for help refactoring some code using streams.



