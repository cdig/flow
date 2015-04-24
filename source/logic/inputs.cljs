;; Look at the raw events, and process them into instructions for what the GUI should do.

(ns logic.inputs)

(defn keyboard-shortcuts
  [world [event-type event-data]]
  (if (= event-type :key-down)
      (let [[changed pressing] event-data]
        (case pressing
              
              #{:z}
                (assoc world :action :undo)
              
              #{:shift :z}
                (assoc world :action :redo)
              
              world))
      (assoc world :action nil)))

(defn- update-mode
  [world [event-type event-data]]
  (or (case event-type
            
            :key-down
              (assoc world :mode (case (first event-data)
                :c :drawing
                :space :navigating
                nil))
                
            :key-up
              (assoc world :mode nil)
            
            nil)
    world))

;; PUBLIC

(defn act
  [world event]
  (-> world
      (keyboard-shortcuts event)
      (update-mode event)))
