# bot manager

An application that allows frequently used carpet bot and their operations to be saved and recalled.

## how to install

1. `/carpet scriptsAppStore TaichiServer/scriptCollection/contents/program`
    This will tell Carpet where to find the script.
1. `/script download bot.sc`
    Download script. You can also be downloaded from within this repository and placed directly in `world/scripts`or`saves/[your world name]/scripts`, but this is deprecated.

## how to use

this script is CLI based. All operations are therefore carried out via commands.

### command
`/bot` : display version and credit<br>
`/bot config create <id> <mcid> <forcible> <command>` : create new script<br>
`/bot config delete <existing_id>` : delete script<br>
`/bot config info` show all available script<br>
`/bot config info <existing_id>`show detailed specific script<br>
`/bot config modify <existing_id> insert <index> <command>` : modify script with insert<br>
`/bot config modify <existing_id> append <command>` : modify script with append<br>
`/bot config modify <existing_id> prepend <command>` : modify script with prepend<br>
`/bot config modify <existing_id> remove index <index>` : remove index line of script<br>
`/bot config modify <existing_id> remove all`<br>
`/bot execute <existing_id>` : remove all command of script, not delete<br>
`/bot query <fake_player>` : show where bot is

### file
script file is in `bot.data/bot/[id].txt`, and this can be created, deleted or modified from outside the game.
This file consists of :
```
mcid
forcible
command
.
.
.
```