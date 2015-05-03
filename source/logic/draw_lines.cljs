(ns logic.draw-lines
  (:require [app.undo :as undo]
            [entity.entity :as entity]
            [facet.facet :as facet]
            [core.math :refer [round]]))

(defn save-state!
  "Returns a world."
  [world]
  (undo/save! (entity/all world))
  world)

(defn- pos->grid-pos
  "Returns a grid pos."
  [pos]
  (zipmap
    (keys pos)
    (map #(round (/ % 30)) (vals pos))))

(defn- create-line
  "Returns a world."
  [world eid points]
  (-> world
      (facet/attach eid :line points)
      (facet/attach eid :stroke-rgb 200)))

(defn- create-point
  "Returns a world."
  [world eid pos]
  (-> world
      (facet/attach eid :grid-pos (pos->grid-pos pos))
      (facet/attach eid :circle 6)
      (facet/attach eid :stroke-rgb 160)))

(defn- start-line
  "Make two points and connect them."
  [world pos]
  (let [head-eid (entity/create!)
        tail-eid (entity/create!)
        line-eid (entity/create!)]
    (print pos)
    (-> world
        (assoc ::drawing tail-eid) ;; This is transient state, not really part of the "world"
        (create-point head-eid pos)
        (create-point tail-eid pos)
        (create-line line-eid [head-eid tail-eid]))))
  
(defn- move-line
  [world pos]
  (facet/attach world (::drawing world) :grid-pos (pos->grid-pos pos)))
  
(defn- end-line
  [world]
  (let [tail-eid (::drawing world)
        world (assoc world ::drawing nil)]
    ;; TODO: if line is 0-length: destroy it; else save-state!
    (save-state! world)))

;; PUBLIC
  
(defn tick
  [world [event-type event-data]]
  (or
    (case event-type
      
      :mouse-down
      (when (= (:mode world) :drawing)
            (start-line world (:abs event-data)))

      :mouse-drag
      (when (::drawing world)
            (move-line world (:abs event-data)))
      
      :mouse-up
      (when (::drawing world)
            (end-line world))

      nil)
    world))
