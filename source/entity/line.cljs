(ns entity.line
  (:require [app.entity :as entity]
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

;; PUBLIC

(defn create!
  "Make and save a new line Entity."
  [& points]
  (entity/create! {:geo {:type :line :points (snap-points points)}
                   :dye {:stroke "#CCC"}}))

(defn edit!
  "Edit and save an existing line."
  [eid & points]
  (entity/update! eid assoc-in [:geo :points] (snap-points points)))

(defn move-tail!
  "Move the tail of an existing line."
  [eid new-tail]
  (entity/update! eid update-in [:geo :points] #(conj (pop %) (snap new-tail))))

(defn length
  "Get the length of the line given by eid."
  [eid]
  (first (reduce (fn [[total prev] point]
                   (if (nil? prev)
                       [total point]
                       [(+ total (distance-between prev point)) point]))
                 [0 nil]
                 (get-in (entity/fetch eid) [:geo :points]))))
