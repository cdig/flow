;; Based on the events, and the current state of the app, update entities.

(ns app.logics
  (:require [app.db :as db]
            [app.entity :as entity]
            [app.viewport :as viewport]))

;; EVENT DETECTION

(defn- event-occurred? [type [k _]]
  (keyword-identical? type k))

(def resize? (partial event-occurred? :resize))
(def mouse-move? (partial event-occurred? :mouse-move))
(def mouse-drag? (partial event-occurred? :mouse-drag))
(def mouse-down? (partial event-occurred? :mouse-down))
(def mouse-up? (partial event-occurred? :mouse-up))

;; PERF HELPERS

(def merge+ (partial merge-with +))

;; LOGICS

(defn- move-viewport [events]
  (let [drag (:mouse-drag events)]
    (when drag
      (viewport/move-pos! (:rel drag)))))

(defn- drag-all-entities [events]
  (let [entities (entity/all)]
    (doseq [[type {:keys [rel]}] events]
      (when (= type :mouse-drag)
        (doseq [[eid entity] entities]
          (entity/update! eid update :pos merge+ rel))))))

(defn- draw-line
  "Draw a line!"
  [events]
  (when (some mouse-down? events)))

(defn- generate-news-for-render
  "Generate whatever data the renderer might need to know"
  [events]
  {:resize? (some resize? events)})

;; MAIN

(defn update-world! [events dT]
  
  ; (move-viewport events)
  ; (drag-all-entities events)

  (draw-line events)
  
  (generate-news-for-render events))
