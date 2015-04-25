(ns app.world)

(defonce world (atom nil))

(defn create
  []
  {})

(defn fetch
  []
  @world)

(defn save!
  [w]
  (reset! world w))
