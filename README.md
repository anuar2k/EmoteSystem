*Note: This module requires some changes [from this PR](https://github.com/MovingBlocks/Terasology/pull/3200) to be merged into the game's engine.*
# EmoteSystem
EmoteSystem is a module, which lets you express emotions in the game! Currently, only customized chat 
messages are avaiable.

To create a new emote, you need a prefab with a piece of code like this:

```json
{
    "EmoteSystem:Emote": {
        "message": "Help me!"
    }
}
```
Other modules also can register their own emotes! (just include a prefab)

#### Usage:

Press **C** key to open a selection screen, move your mouse cursor over the emote you want to express and release the key. 

Press **F9** key to open the configuration screen (currently it's just a dummy).