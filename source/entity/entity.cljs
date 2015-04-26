(ns entity.entity
  (:require [app.ider :as ider]
            [facet.all :as facet]))

(defn- make
  "Make a new, empty entity. This deserves to be a function, so that we have an easy place to see (and change) the default entity structure."
  [eid]
  {:eid eid})

;; PUBLIC

(defn all
  "Takes a world. Returns a map of all entities in the world."
  [world]
  (::entities world))

(defn fetch
  "Takes a world and an eid. Returns the corresponding entity, or nil."
  [world eid]
  (get-in world [::entities eid]))

(defn entity->renderable
  "Turns a entity into renderable data, which tells the surface what to draw."
  [entity]
  (reduce merge (vals (dissoc entity :eid))))
  
(defn save
  "Takes a world and a entity, and saves the entity into the world. Returns the updated world."
  [world entity]
  (assoc-in world [::entities (:eid entity)] entity))

(defn attach-facets
  "Creates and attaches many facets to an entity. Takes a world, entity id, and a map of facet types to initial values. Creates each facet and attaches it to the entity. Returns the updated world."
  [world eid facets]
  (loop [world world facets facets]
    (if-let [[type init] (first facets)]
      (recur (assoc-in world [::entities eid type] (facet/create type init))
             (rest facets))
      world)))

(defn create
  "Takes a world and a map of facet names to initial values. Creates a new entity, saves it into the world, and attaches all the facets. Returns the updated world. If you would like to have a reference to the new entity, please include an :eid in the map of facets."
  [world facets]
  (let [eid (or (:eid facets) (ider/get-next! :eid))]
    (-> world
        (save (make eid))
        (attach-facets eid (dissoc facets :eid)))))
