;; Objects are just a map of state, with an oid (Object ID) to uniquely identify them.

(ns object.object
  (:require [core.id :as id]))

(defn- make
  "Make a new, empty object. This deserves to be a function, so that we have an easy place to see (and change) the default object structure."
  [oid]
  {:oid oid})

(defn- get-oid
  [world {oid :oid}]
  (if oid
      [world oid]
      (id/make-id world :oid)))

;; PUBLIC

(defn save
  "Takes a world and an object, and saves the object into the world. Returns the updated world."
  [world object]
  (assoc-in world [::objects (:oid object)] object))

(defn fetch
  "Takes a world and an oid. Returns the object for that oid."
  [world oid]
  (get-in world [::objects oid]))

(defn change
  "Takes a world, oid, function, and arguments. Applies the fn to the oid's object and the args, saves, and returns the updated world."
  [world oid f & args]
  (save world (apply f (fetch world oid) args)))

(defn all
  "Takes a world. Returns a map of all objects in the world."
  [world]
  (::objects world))

(defn populate
  "Takes a world and a map of objects. Returns a world that only contains those objects."
  [world objects]
  (assoc world ::objects objects))

(defn object->renderable
  "Turns an object into renderable data, which tells the surface what to draw."
  [object]
  (merge
    (:pos object)
    (:geo object)
    (:dye object)))

(defn create-and-use
  "Create a new object. Takes a world and a template object. Returns a three element vector with the updated world, the new object, and its oid."
  [world template]
  (let [[new-world oid] (get-oid world template)
        object (merge (make oid) template)]
    [(save new-world object) object oid]))

(defn create
  "Create a new object. Takes a world and a template object. Returns the updated world. To also get the new object, use the function create-and-use instead."
  [world template]
  (let [[new-world new-object new-oid] (create-and-use world template)]
    new-world))

(defn destroy
  "Destroys an object. Takes a world and an oid. Returns the world, sans that object."
  [world oid]
  (update world ::objects dissoc oid))

(defn setup
  [world]
  (assoc world ::objects {}))
  
