;; Entities are the GUI equivalent of Objects; a map of state, with an eid (Entity ID) to uniquely identify them.

(ns entity.entity
  (:require [app.ider :as ider]))

(defn- make
  "Make a new, empty entity. This deserves to be a function, so that we have an easy place to see (and change) the default entity structure."
  [eid]
  {:eid eid})

;; PUBLIC

(defn save
  "Takes a world and an entity, and saves the entity into the world. Returns the updated world."
  [world entity]
  (assoc-in world [::entities (:eid entity)] entity))

(defn fetch
  "Takes a world and an eid. Returns the entity for that eid."
  [world eid]
  (get-in world [::entities eid]))

(defn change
  "Takes a world, eid, function, and arguments. Applies the fn to the eid's entity and the args, saves, and returns the updated world."
  [world eid f & args]
  (save world (apply f (fetch world eid) args)))

(defn all
  "Takes a world. Returns a map of all entities in the world."
  [world]
  (::entities world))

(defn populate
  "Takes a world and a map of entities. Returns a world that only contains those entities."
  [world entities]
  (assoc world ::entities entities))

(defn entity->renderable
  "Turns an entity into renderable data, which tells the surface what to draw."
  [entity]
  (merge
    (:pos entity)
    (:geo entity)
    (:dye entity)))

(defn create-and-use
  "Create a new entity. Takes a world and a template entity. Returns a three element vector with the updated world, the new entity, and its eid. DEPRECATED — if you care about using the entity, you should make the ID yourself, and hang on to it."
  [world template]
  (let [eid (or (:eid template) (ider/get-next! :eid))
        entity (merge (make eid) template)]
    [(save world entity) entity eid]))

(defn create
  "Create a new entity. Takes a world and a template entity. Returns the updated world. To also get the new entity, use the function create-and-use instead."
  [world template]
  (let [[new-world new-entity new-eid] (create-and-use world template)]
    new-world))

(defn destroy
  "Destroys an entity. Takes a world and an eid. Returns the world, sans that entity."
  [world eid]
  (update world ::entities dissoc eid))

(defn setup
  [world]
  (assoc world ::entities {}))
  
