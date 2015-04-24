(ns object.line
  (:require [object.object :as object]
            [core.math :refer [round sqrt]]))

(defn- snap [[x y]]
  [(* (round (/ x 30)) 30)
   (* (round (/ y 30)) 30)])

(defn- snap-points [points]
  (vec (map snap points)))

(defn- distance-between
  "Pythagorean yeah!"
  [[ax ay] [bx by]]
  (let [x (- ax bx)
        y (- ay by)]
    (sqrt (+ (* x x) (* y y)))))

(defn- length-reduce
  [[total prev] point]
  (if (nil? prev)
      [total point]
      [(+ total (distance-between prev point)) point]))

;; PUBLIC

(defn create-and-use
  "Make a new line Object. Takes a world and many points. Returns a 3 element vector with the updated world, the new object, and its oid."
  [world & points]
  (object/create-and-use world
                 {:geo {:type :line :points (snap-points points)}
                  :dye {:stroke "#CCC"}}))

(defn create
  "Make a new line Object. Takes a world and many points. Returns the updated world."
  [world & points]
  (object/create world
                 {:geo {:type :line :points (snap-points points)}
                  :dye {:stroke "#CCC"}}))

(defn edit
  "Edit and save an existing line. Takes a world, an oid, and many points. Returns the updated world."
  [world oid & points]
  (let [object (object/fetch world oid)]
    (object/save world (assoc-in object [:geo :points] (snap-points points)))))

(defn move-tail
  "Move the tail of an existing line. Takes a world, an oid, and many points. Returns the updated world."
  [world oid new-tail]
  (let [object (object/fetch world oid)]
    (object/save world (update-in object [:geo :points] #(conj (pop %) (snap new-tail))))))

(defn length
  "Takes a world and a line's oid, and returns the length of the line."
  ;; We could probably better write this with loop/recur rather than reduce
  [world oid]
  (first (reduce length-reduce
                 [0 nil]
                 (get-in (object/fetch world oid) [:geo :points]))))
