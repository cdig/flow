;; Entities are the objects in our system.
;; They are just a map of state, with an eid (Entity ID) to uniquely identify them.

(ns app.entity
  (:require [app.db :as db]
            [app.id :as id]))

(defn- make
  "Make a new, empty Entity. This deserves to be a function, so that we have an easy place to see (and change) the default entity structure."
  [eid]
  {:eid eid})

;; PUBLIC

(defn create!
  "Make and save a new Entity, optionally populated with a given map of default values. Returns the new entity's eid."
  [defaults]
  (let [eid (or (:eid defaults) (id/make-id! :eid))
        entity (merge (make eid) defaults)]
    (db/update-state! ::entities assoc eid entity)
    eid))

(defn update!
  "Update the entity."
  [eid f & args]
  (apply db/update-state! ::entities update eid f args))

(defn all
  "Returns a vec of all entities in the system."
  []
  (db/get-state ::entities))

(defn- entity->renderable
  "Turns an entity into renderable data, which tells the surface what to draw."
  [entity]
  (merge
    (:pos entity)
    (:geo entity)
    (:dye entity)))
