;; The render system is responsible for orchestrating how our entities get drawn to the screen. It takes the entities that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns app.render
  (:require [entity.entity :as entity]
            [gui.viewport :as viewport]
            [browser.window :as window]
            [io.surface :as surface]))

(defonce layer-state (atom {}))

(defn check-resize!
  [world]
  (when (get-in world [:event-set :resize])
    (reset! layer-state {})))

(defn render!
  "Takes a layer keyword, some state for comparison, and a seq of renderables. If the state is different than the previous state for this layer, renders the renderables to the canvas for this layer."
  [layer new-state renderables]
  (when-not (= new-state (get @layer-state layer))
    (swap! layer-state assoc layer new-state)
    (surface/render!
      layer
      (window/width)
      (window/height)
      renderables)))

(defn act!
  "When stuff changes, draw it!"
  [world]
  (check-resize! world)
  (render! ::viewport
           (viewport/get-pos world)
           (viewport/renderable world))
  (render! ::entity
           (entity/all world)
           (map (partial entity/entity->renderable world) (vals (entity/all world))))
  world)
