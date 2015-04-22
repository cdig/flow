(ns handlers.all
  (:require [handlers.keyboard]
            [handlers.mouse]
            [handlers.resize]))

(defn setup! []
  (handlers.keyboard/setup!)
  (handlers.mouse/setup!)
  (handlers.resize/setup!))
