# Flow

This is a prototype Live Schematic editor. It's very rough and should be ignored. I'm just putting it here so that it won't be lost if anything happens to my laptop.



# Design

The system is a testbed for a bunch of ideas.

#### Component/Entity/System — A la LightTable's BOT, Unity, etc
*The world is comprised of entities, which have components, which are manipulated by systems.*

We keep the entities pretty much as-is — they're simply a unique ID for a block of memory. We call our components "facets" to avoid confusing them with hydraulic components. Facets are both code and data — they perform some of the role of systems. But we also have some conventional systems for things like orchestration and IO. If you squint, our implementation is like the bastard child of CES and MVC and OOP. That sounds pretty horrible, so maybe don't think about it that way.

#### Out of the Tar Pit's FRP (Functional Relational Programming)
*Logic and Data are strictly separated, and utterly pure. The data is modelled relationally (as mathematical sets). The logic is a hybrid of logic programming and pure functions.*

I'm using a few of these ideas for the implementation of the CES design, but I'd really like to draw on this thinking when exposing a programming interface to the end user (schematic creator). This paper was a fucking goldmine.

#### Conal Elliott's FRP (Functional Reactive Programming)
*Values in the system aren't raw data — they are be "behaviours" that have continuous time semantics and are a function of the reals (time) to a value. "Events" swap which behaviours are being used at discrete moments in time.*

This stuff isn't really implemented yet, but it's in the cards. I'm really attracted to the idea of running my simulation in continuous time. We will call them "signals" instead of "behaviours". We will use composable signals as an alternative to primitive (constant) values, where appropriate. They'll offer the benefits of the mapping/shaping functions I've been using in my audio software experiments.

#### Rx/Elm FRP (Functional Reactive Programming)
*Behaviours/Signals have functor-like semantics.*

Not implemented yet. I have no clue what sort of UI to supply for manipulating signals and connecting them into a dependency/flow graph, but I likely don't want it to be based on lists (a la 3DSMax's wired parameters) and I'm pretty sure I don't want it to be standard boxes-and-lines visual programming (a la Max/MSP). It might be nice if the connections and relationships were exposed visually directly on top of the entities in the world. Just throwing out ideas.



# Implementation

## How to talk about Entities and Facets

These are a bit difficult to talk about, because they are both *conceptual* and *actual* things. It's like the difference between a class (conceptual) and an instance (actual). In our case, it's the difference between the code, and the data. Conversationally, to tell the two apart, we often refer to the data as an instance.

#### Here's what they *are* (data):

An **entity** is a data structure in memory. We give entities a unique entity ID (eid), and use that for random access to them. In addition to storing their own eid, entities also contain facets.

A **facet** is a data structure in memory. Their identity comes from the entity that they belong to. They are stored within the entity by type, so that we do not have duplicates.

#### Here's what they *do* (code):

An entity doesn't *do* anything. It's just data. There are some functions for CRUD-like operations, but they're just for convenience.

Facets also describe their own behaviour. Each facet has a code file that includes two common interface functions. The `create` function takes some state data (of any type) that will be provided by whoever is creating the facet, and returns a facet data structure. The `render` function takes a world and a facet data structure, and returns a representation of the facet data appropriate for rendering.

#### Facets are dumb

It's very important to note that no code (including references to functions) is *ever* stored as part of the data of the program. All dispatch is static. The data is dumb. This lines up nicely with some of the thinking in Out of the Tar Pit, and gives us an air gap away from OOP. It means we can naively marshal the data to and from text.

#### Facets are confused

The reason we've got two meanings for "facet" (code, and data) is to attempt to make clear the relationship between them — as counterintuitive as this may seem. If we instead went with the traditional Component VS System design, then it might be hard to tell if two systems were fighting over the same component, or it might be hard to decide where to put system behaviour that applied to only one component.


#### Talking about Entities colloquially — "Is" vs "Has"
  
When we say an entity "is" something, we just mean the entity "has" a particular facet.

| Example | Meaning |
|---------|---------|
| "this entity is a sphere" | "this entity has a geo/sphere facet"         |
| "the viewport"            | "the entity that has the gui/viewport facet" |
  
These entities might have other facets too — say, a position or a name.

This phrasing matches up nicely with the real world. You would say "I am a woman" or "I am a doctor" because you are defining yourself by those facets of yourself. In *Identity/State/Value* terms, it's using a part of the *state* as a shorthand *identity*, like a pronoun or a nickname.



## How stuff shows up on screen

### Assumptions

For now, we will assume many canvases (for layers) and no other DOM.
* Revisions to support DOM (eg: buttons) will be far-future (ie: require extensive rewriting).
* "DOM" is synonymous with "platform UI" (eg: UIKit gui objects on iOS).
  
For now, we will assume one viewport, but will code as if there were more than one.
* Multi-viewport should be easy enough to add if the need arises.
* Each viewport should probably have its own layer canvases, so we can easily support non-rectangular arrangements or multi-monitor.

### Architecture

A **Viewport** is an entity with a gui/viewport facet and a pos/screen-rect facet.

A gui/viewport facet references a **Camera** entity (which may be swapped).

Cameras are just entities with a pos/ (maybe a data/name, too?).
* So you can use any entity as a camera.
* There could also be a geo/camera facet so we can see where (other) cameras are in our viewport.

All renderable entities have a layer (possibly the default), which corresponds to the canvas element we use to draw it.
  Each layer is drawn (and cached) individually.
  
  
  
# TODO
  
#### Long term
  
* Support for behaviours instead of primitive values in facet properties
* * So like.. when you're drawing a line.. the position of the tail is wired like..
* * * mouse position -> snap to grid function -> tail position
* * And then instead of updating it every mouse move, we just set it up on mouse-down, and then switch it to a static behaviour on mouse-up
* Entities can have a "selectable" facet
* Selected things can be moved by switching their position to pull from the mouse behaviour
* multiple selection
* continuous time simulation
