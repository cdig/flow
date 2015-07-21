;; The render system is responsible for orchestrating how our entities get drawn to the screen. It takes the entities that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns app.render
  (:require [entity.entity :as entity]
            [facet.facet :as facet]
            [gui.viewport :as viewport]
            [browser.window :as window]
            [io.surface :as surface]))

;; PUBLIC

(defn act!
  "Loop over the viewports and tell each of them to render."
  [world]
  (doseq [v (entity/all world :viewport)]
    (viewport/render! world v)))
