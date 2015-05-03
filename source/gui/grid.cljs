(ns gui.grid)

(defn get-pitch
  [world]
  (::pitch world))

(defn get-major
  [world]
  (::major world))

(defn set-pitch
  [world pitch]
  (assoc world ::pitch pitch))

(defn set-major
  [world major]
  (assoc world ::major major))

(defn setup
  [world]
  (-> world
    (set-pitch 30)
    (set-major 5)))
