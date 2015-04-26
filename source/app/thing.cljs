(ns app.thing
  (:require [app.ider :as ider]
            [facet.all :as facet]))

(defn- make
  "Make a new, empty thing. This deserves to be a function, so that we have an easy place to see (and change) the default thing structure."
  [tid]
  {:tid tid})

(defn- attach-facets
  "Creates and attaches many facets to an entity. Takes a world, thing id, and a map of facet types to initial values. Creates each facet and attaches it to the entity. Returns the updated world."
  [world tid facets]
  (loop [world world facets facets]
    (if-let [[type init] (first facets)]
      (recur (facet/create world tid type init) (rest facets))
      world)))

;; PUBLIC

(defn all
  "Takes a world. Returns a map of all things in the world."
  [world]
  (::things world))


(defn thing->renderable
  "Turns an thing into renderable data, which tells the surface what to draw."
  [thing]
  ; (merge
  ;   (:pos thing)
  ;   (:geo thing)
  ;   (:dye thing))
  )
  
  
(defn save
  "Takes a world and a thing, and saves the thing into the world. Returns the updated world."
  [world thing]
  (assoc-in world [::things (:tid thing)] thing))

(defn create
  "Takes a world and a map of facet names to initial values. Creates a new thing, saves it into the world, and attaches all the facets. Returns the updated world. If you would like to have a reference to the new thing, please include an :tid in the map of facets."
  [world facets]
  (let [tid (or (:tid facets) (ider/get-next! :tid))
        facets (dissoc facets :tid)]
    (save world (make tid))
    (attach-facets world tid (dissoc facets :tid))))
