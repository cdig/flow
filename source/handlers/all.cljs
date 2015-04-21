(ns handlers.all
  (:require [handlers.mouse]
            [handlers.resize]))

(defn setup! []
  (handlers.mouse/setup!)
  (handlers.resize/setup!))
