;; The render system is responsible for orchestrating how our entities get drawn to the screen. It takes the entities that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns render.render
  (:require [entity.entity :as entity]
            [gui.viewport :as viewport]
            [browser.window :as window]
            [render.surface :as surface]))

(defonce prev-viewport (atom nil))
(defonce prev-entities (atom nil))

(defn check-resize!
  [world]
  (when (get-in world [:event-set :resize])
    (reset! prev-viewport nil)
    (reset! prev-entities nil)))

(defn render-viewport!
  [world]
  (let [pos (viewport/get-pos world)]
    (when-not (= pos @prev-viewport)
      (reset! prev-viewport pos)
      (surface/render!
        ::viewport-layer
        (window/width)
        (window/height)
        (viewport/renderable world)))))

(defn render-entities!
  [world]
  (let [entities (entity/all world)]
    (when-not (= entities @prev-entities)
      (reset! prev-entities entities)
      (surface/render!
        ::entities-layer
        (window/width)
        (window/height)
        (map entity/entity->renderable (vals entities))))))


(defn act!
  "When stuff changes, draw it!"
  [world]
  (check-resize! world)
  (render-viewport! world)
  (render-entities! world)
  world)
