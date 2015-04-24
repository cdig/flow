;; Wraps the browser keyboard APIs

;; Side Effects X
;; State X
;; Deps —

(ns web.keyboard)

;; NICENESS

(defn event->keyword [event]
  (case (.-keyCode event)
    8 :delete
    9 :tab
    13 :return
    16 :shift
    17 :ctrl
    18 :alt
    20 :capslock
    32 :space
    
    37 :left
    38 :up
    39 :right
    40 :down
    
    48 :0
    49 :1
    50 :2
    51 :3
    52 :4
    53 :5
    54 :6
    55 :7
    56 :8
    57 :9
    
    65 :a
    66 :b
    67 :c
    68 :d
    69 :e
    70 :f
    71 :g
    72 :h
    73 :i
    74 :j
    75 :k
    76 :l
    77 :m
    78 :n
    79 :o
    80 :p
    81 :q
    82 :r
    83 :s
    84 :t
    85 :u
    86 :v
    87 :w
    88 :x
    89 :y
    90 :z
    
    91 :meta ; left
    93 :meta ; right
    
    186 :semicolon
    187 :equals
    188 :comma
    189 :minus
    190 :period
    191 :forwardslash
    192 :tilde
    219 :leftsquarebracket
    220 :backslash
    221 :rightsquarebracket
    222 :quote

    nil))
