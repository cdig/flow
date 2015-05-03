(ns entity.entity
  (:require [app.ider :as ider]))

(defn create!
  "Takes nothing, returns a new eid."
  []
  (ider/get-next! :eid))

(defn populate
  "Takes a world, a type, and a map of entities. Replaces all existing entities of that type in the world with the new map. Returns an updated world."
  [world type entities]
  (assoc-in world [::entities type] entities))

(defn all
  "Returns all entities of a given type in the given world."
  [world type]
  (get-in world [::entities type]))

(defn fetch
  "Returns the entity in the world given by type and eid."
  [world type eid]
  (get-in world [::entities type eid]))

(defn store
  [world type eid entity]
  (assoc-in world [::entities type eid] entity))
