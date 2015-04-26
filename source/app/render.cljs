;; The render system is responsible for orchestrating how our objects get drawn to the screen. It takes the objects that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns app.render
  (:require [object.object :as object]
            [app.thing :as thing]
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
  (render! ::object
           (object/all world)
           (map object/object->renderable (vals (object/all world))))
  (render! ::thing
           (thing/all world)
           (map thing/thing->renderable (vals (thing/all world))))
  
  world)
