(ns entity.entity
  (:require [app.ider :as ider]))

(defn create!
  "Takes nothing, returns a new eid."
  []
  (ider/get-next! :eid))

(defn populate
  "Takes a world and a map of entities. Replaces all existing entities in the world with the new map. Returns an updated world."
  [world entities]
  (assoc world :entities entities))

(defn all
  "Returns all entities in the given world."
  [world]
  (:entities world))

(defn fetch
  "Returns the entity in the world given by eid."
  [world eid]
  (get-in world [:entities eid]))

(defn store
  [world eid entity]
  (assoc-in world [:entities eid] entity))
