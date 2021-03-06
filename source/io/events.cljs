(ns io.events
  (:require [browser.events :refer [addWindowListener!]]
            [browser.keyboard :as keyboard]
            [browser.mouse :as mouse]
            [browser.window :as window]))

;; Helpers

(defn- addListener!
  "Returns nil."
  [event-name handler]
  (addWindowListener!
    event-name
    #(doto %
      ; .preventDefault ;; We will want to turn this on.. eventually? Maybe? It messes with standard browser behaviour
      .stopPropagation
      handler)))

(defn improve-mouse-data
  [world old-world abs]
  (let [rel (merge-with - abs (get-in old-world [:input :mouse :pos]))]
    {:abs abs
     :rel rel}))

(defn- improve-data
  "Returns improved event data."
  [world old-world event-name event-data]
  (case event-name
    :mouse-move (improve-mouse-data world old-world event-data)
    :mouse-drag (improve-mouse-data world old-world event-data)
    :mouse-down (improve-mouse-data world old-world event-data)
    :mouse-up   (improve-mouse-data world old-world event-data)
    event-data))

(defn- improve-name
  "Returns an improved name."
  [world event-name]
  (if (and (= event-name :mouse-move)
           (get-in world [:input :mouse :down]))
      :mouse-drag
      event-name))

(defn update-world-event
  "Returns a world."
  [world old-world event-name event-data]
  (let [event-name (improve-name world event-name)
        event-data (improve-data world old-world event-name event-data)]
    (assoc world :event [event-name event-data])))

(defn- update-world-input
  "Returns a world."
  [world event-name event-data]
  (case event-name
    :mouse-move (assoc-in world [:input :mouse :pos] event-data)
    :mouse-down (assoc-in world [:input :mouse :down] true)
    :mouse-up   (assoc-in world [:input :mouse :down] false)
    :key-down   (update-in world [:input :keyboard] conj event-data)
    :key-up     (update-in world [:input :keyboard] disj event-data)
    :resize     (assoc-in world [:input :window] event-data)))

(defn- handle-event!
  "Returns nil."
  [old-world give-world event-name event-data]
  (let [new-world (update-world-input old-world event-name event-data)]
    (when (not= old-world new-world) ;; Only continue if the event provided new input data to the system
      (-> new-world
          (update-world-event old-world event-name event-data)
          give-world))))

(defn- setup-listeners!
  "Returns nil."
  [get-world give-world]
  (addListener! "mousemove" #(handle-event! (get-world) give-world :mouse-move (mouse/event->pos %)))
  (addListener! "mousedown" #(handle-event! (get-world) give-world :mouse-down (mouse/event->pos %)))
  (addListener! "mouseup"   #(handle-event! (get-world) give-world :mouse-up   (mouse/event->pos %)))
  (addListener! "keydown"   #(handle-event! (get-world) give-world :key-down   (keyboard/event->keyword %)))
  (addListener! "keyup"     #(handle-event! (get-world) give-world :key-up     (keyboard/event->keyword %)))
  (addListener! "resize"    #(handle-event! (get-world) give-world :resize     (window/current-size %))))

(defn- setup-world-input
  "Returns a world."
  [world]
  (assoc world :input
    {:mouse {:pos {:x 0 :y 0}
             :down false}
     :keyboard #{}
     :window (window/current-size)}))
  
;; PUBLIC

(defn setup!
  "Takes a world and two functions to be used when an event fires — one that gets the current world, and another that will be called with the updated world. Creates input event listeners, and adds input state fields to the world. Returns an updated world."
  [world get-world give-world]
  (setup-listeners! get-world give-world)
  (setup-world-input world))
