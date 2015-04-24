(ns entity.line
  (:require [entity.entity :as entity]
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
  "Make a new line Entity. Takes a world and many points. Returns a 3 element vector with the updated world, the new entity, and its eid."
  [world & points]
  (entity/create-and-use world
                 {:geo {:type :line :points (snap-points points)}
                  :dye {:stroke "#CCC"}}))

(defn create
  "Make a new line Entity. Takes a world and many points. Returns the updated world."
  [world & points]
  (entity/create world
                 {:geo {:type :line :points (snap-points points)}
                  :dye {:stroke "#CCC"}}))

(defn edit
  "Edit and save an existing line. Takes a world, an eid, and many points. Returns the updated world."
  [world eid & points]
  (let [entity (entity/fetch world eid)]
    (entity/save world (assoc-in entity [:geo :points] (snap-points points)))))

(defn move-tail
  "Move the tail of an existing line. Takes a world, an eid, and many points. Returns the updated world."
  [world eid new-tail]
  (let [entity (entity/fetch world eid)]
    (entity/save world (update-in entity [:geo :points] #(conj (pop %) (snap new-tail))))))

(defn length
  "Takes a world and a line's eid, and returns the length of the line."
  ;; We could probably better write this with loop/recur rather than reduce
  [world eid]
  (first (reduce length-reduce
                 [0 nil]
                 (get-in (entity/fetch world eid) [:geo :points]))))
