;; The render system is responsible for orchestrating how our entities get drawn to the screen. It takes the entities that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns app.render
  (:require [app.db :as db]
            [app.entity :as entity]
            [app.viewport :as viewport]
            [browser.window :as window]
            [graphics.surface :as surface]))

(defn render-world!
  "When stuff changes, draw it!"
  [news]
  
  (when (:resize news)
        (db/set-cache! ::viewport-pos nil)
        (db/set-cache! ::entities nil))
  
  (let [pos (viewport/get-pos)]
    (when-not (= pos (db/get-cache ::viewport-pos))
      (db/set-cache! ::viewport-pos pos)
      (surface/render!
        ::viewport-layer
        (window/width)
        (window/height)
        (viewport/renderable))))

  (let [entities (entity/all)]
    (when-not (= entities (db/get-cache ::entities))
      (db/set-cache! ::entities entities)
      (surface/render!
        ::entities-layer
        (window/width)
        (window/height)
        (map entity/entity->renderable (vals entities))))))
