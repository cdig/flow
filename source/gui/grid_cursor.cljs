(ns gui.grid-cursor
  (:require [entity.entity :as entity]
            [core.math :refer [round]]))

;; Visibility

(defn- mode->stroke
  [mode]
  (if (= mode :drawing)
      "hsla(212,24%,32%,1)"
      "hsla(212,24%,32%,0)"))

(defn- set-visibility
  [world [event-type event-data]]
  (if (or (= event-type :key-down)
          (= event-type :key-up))
      (entity/update-eid world ::grid-cursor {:dye {:stroke (mode->stroke (:mode world))}})
      world))

;; Positioning

(defn- pos->grid-pos
  [pos]
  (zipmap
    (keys pos)
    (map #(round (/ % 30)) (vals pos))))

(defn- check-move
  [world [event-type event-data]]
  ;; It'd be nice to express a query on the world, and when the result of the query changes, THEN we re-run our logic.
  ;; That might help make these logic statements more readable, since that's the form they tend to take (query -> action)
  ;; Eg: the query here is a check first on the mode, then on the mouse pos.
  ;; The query for set-visibility is a check on the mode (but currently, we have to check for key presses because we aren't saving a previous mode state to compare to).
  (if (and (= (:mode world) :drawing)
           (or (= event-type :mouse-move) ;; We also want to add key-down here, but we need access to current mouse state.
               (= event-type :mouse-drag)))
      (entity/update-eid world ::grid-cursor {:grid-pos (pos->grid-pos (:abs event-data))})
      world))

;; PUBLIC

(defn setup
  [world]
  (entity/create world {:eid ::grid-cursor
                        :grid-pos nil
                        :circle 30
                        :dye {:stroke "hsla(212,24%,32%,0)"}}))

(defn tick
  [world event]
  (-> world
      (set-visibility event)
      (check-move event)))
