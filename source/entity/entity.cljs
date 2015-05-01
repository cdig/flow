;;;; Entitiy
;; Just the one file for now, I guess. I wonder what else will someday live in this namespace.
;; Currently, we're assuming an entity is a map with the following characteristics:
;; * Doesn't contain any functions — this isn't OO, kids
;; * All facet data is denormalized inside the entitiy. If we dissoc the entity, there's no other cleanup needed.
;; * Facets from one entity can reference other entities. So.. um.. how does that play with the line above? Hmm?? Dunno.

(ns entity.entity
  (:require [app.ider :as ider]
            [facet.all :as facet]))

(defn- prep-eid
  [facets]
  (let [eid (or (:eid facets)
                (ider/get-next! :eid))]
    (assoc facets :eid eid)))

;; PUBLIC

;; entity -> X

(defn attach-facet
  "Attaches a facet instance to an entity. Returns an updated entity."
  [entity type facet]
  (assoc entity type facet))
  
(defn build-facet
  "Creates and attaches a facet to an entity. Takes an entity, a facet type keyword, and whatever init value should be used. Returns an updated entity."
  [entity type init]
  (attach-facet entity type (facet/create type init)))

(defn build-facets
  "Creates and attaches many facets to an entity. Takes an entity, and a map of facet types to initial values. Creates each facet and attaches it to the entity. Returns an updated entity."
  [entity facets]
  (loop [entity entity facets facets]
    (if-let [[type init] (first facets)]
      (recur (build-facet entity type init)
             (rest facets))
      entity)))

;; world -> entity

(defn all
  "Takes a world. Returns a map of all entities in the world."
  [world]
  (::entities world))

(defn fetch
  "Takes a world and an eid. Returns an corresponding entity, or nil."
  [world eid]
  (get-in world [::entities eid]))

(defn entity->renderable
  "Turns a entity into renderable data, which tells the surface what to draw."
  [world entity]
  (reduce merge (map (partial facet/render world) entity)))

;; world -> world

(defn save
  "Takes a world and a entity, and saves the entity into the world. Returns an updated world."
  [world entity]
  (assoc-in world [::entities (:eid entity)] entity))

(defn populate
  "Takes a world and a map of entities. Replaces all existing entities in the world with the new map. Returns an updated world."
  [world entities]
  (assoc world ::entities entities))

(defn update-entity
  "Changes an entity in the world. Takes a world, entity, and map of facet init values to build and attach to the entity. Returns an updated world."
  [world entity facets]
  (save world (build-facets entity facets)))

(defn update-eid
  "Changes an entity in the world, given by eid. Takes a world, eid, and a map of facet init values to build and attach to the entity. Returns an updated world."
  [world eid facets]
  (update-entity world (fetch world eid) facets))

(defn create
  "Creates a new entity. Takes a world, and a map of facet init values to build and attach to the new entity. If you would like to have a reference to the new entity, please include an :eid in the map of facet init values. Returns an updated world."
  [world facets]
  (update-entity world {} (prep-eid facets)))

(defn destroy-eid
  "Destroys an entity and all associated facets. Takes a world and an eid. Returns an updated world."
  [world eid]
  (print "need to implement destroy")
  world)
