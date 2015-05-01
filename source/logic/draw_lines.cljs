(ns logic.draw-lines
  (:require [app.ider :as ider]
            [app.undo :as undo]
            [entity.entity :as entity]
            [core.math :refer [round]]))


(defn save-state!
  [world]
  (undo/save! (entity/all world))
  world)

(defn- pos->grid-pos
  [pos]
  (zipmap
    (keys pos)
    (map #(round (/ % 30)) (vals pos))))

(defn- create-line
  [world eid points]
  (entity/create world {:eid eid
                        :line points
                        :stroke-rgb 200}))

(defn- create-point
  [world eid pos]
  (entity/create world {:eid eid
                        :grid-pos (pos->grid-pos pos)
                        :circle 6
                        :stroke-rgb 160}))

(defn- start-line
  "Make two points and connect them."
  [world pos]
  (let [head-eid (ider/get-next! :eid)
        tail-eid (ider/get-next! :eid)
        line-eid (ider/get-next! :eid)]
    (-> world
        (assoc ::drawing tail-eid) ;; This is transient state, not really part of the "world"
        (create-point head-eid pos)
        (create-point tail-eid pos)
        (create-line line-eid [head-eid tail-eid]))))
  
(defn- move-line
  [world pos]
  (entity/update-eid world (::drawing world) {:grid-pos (pos->grid-pos pos)}))
  
(defn- end-line
  [world]
  (let [tail-eid (::drawing world)
        world (assoc world ::drawing nil)]
    (save-state! world)))

;; PUBLIC
  
(defn tick
  [world [event-type event-data]]
  (or
    (case event-type

      :mouse-down (when (= (:mode world) :drawing)
                    (start-line world (:abs event-data)))

      :mouse-drag (when (::drawing world)
                    (move-line world (:abs event-data)))
      
      :mouse-up (when (::drawing world)
                  (end-line world))

      nil)
    world))
