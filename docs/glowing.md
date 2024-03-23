# glowing

Highlight the player with giving a glowing effect by command.

## how to install

1. `/carpet scriptsAppStore TaichiServer/scriptCollection/contents/program`
    This will tell Carpet where to find the script.
1. `/script download glowing.sc`
    Download script. You can also be downloaded from within this repository and placed directly in `world/scripts`or`saves/[your world name]/scripts`, but this is deprecated.

## how to use

This script can only be used with the command. See bellow.

### command
`/glowing give <players> [<duration>] [<show_particles>] [<show_icon>]` : give a glowing effect to the `<players>`
- `<players>`
Must be player entity. Supports multiple entities.
- `[<duration>]`
Must be between `0` and `1000000`. Duration of the effect in seconds. Default value is `5`.
- `[<show_partiles>]`
Must be boolean. If this is `true`, particles are shown. Default value is `false`.
- `[<show_icon>]`
Must be boolean. If this is `true`, effect icon is shown. Default value is `false`.