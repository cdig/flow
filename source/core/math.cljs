;; Side Effects X
;; State X
;; Deps √

(ns core.math
  (:require [browser.math :as math]))

(defn normalize [in inMin inMax]
  (/ (- in inMin)
     (- inMax inMin)))

(defn denormalize [in outMin outMax]
  (-> outMax
      (- outMin)
      (* in)
      (+ outMin)))

(defn scale [in inMin inMax outMin outMax]
  (-> in (normalize inMin inMax)
         (denormalize outMin outMax)))
   
(defn phasor [phase speed func min max]
  (-> phase
      (* speed)
      func
      (denormalize min max)))

(defn cos [i] (math/cos (* i 2 math/PI)))
(defn sin [i] (math/sin (* i 2 math/PI)))

(defn cosn [i] (normalize (cos i) -1 1))
(defn sinn [i] (normalize (sin i) -1 1))

(defn pow [b e] (math/pow b e))
(defn sqrt [i] (math/sqrt i))
(defn round [i] (math/round i))
(defn random [] (math/random))

(def TAU (* 2 math/PI))
