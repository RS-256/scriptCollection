global_app_name = system_info('app_name');
global_scriptVersion = '1.0.0';
global_author = 'RS256';
global_defaultDirectory = global_app_name + '/';

global_help_page = [
	'/%appname%'
];

_error(error) -> exit(print(format(str('r %s', error))));

__config() -> {
	'commands' ->{
		'' -> 'credit',
		'<start> <end>' -> 'replaceWithinVolume'
	},
	'arguments' -> {
		'start' -> {
			'type' -> 'pos'
		},
		'end' -> {
		    'type' -> 'pos'
		}
	}
};

credit() -> (
	text = 'version : ' + global_scriptVersion + '\nauthor : ' + global_author;
	print(text);
);

replaceWithinVolume(start, end) -> (
    if(player()~'permission_level' <= 2, _error('you must have op level 2'));
    without_updates(volume(start, end, replaceBlock(_)));
    text = format('g replaced double slabs to block within the area');
    print(player('all'),text);
);

doubleSlab2Block(Block) -> (
    if(block_tags(Block, 'slabs') == true && block_state(Block, 'type') == 'double',
        replaceWith = replace(Block, '_slab', '');
        replaceWith = replace(replaceWith, 'brick', 'bricks');
        replaceWith = replace(replaceWith, 'tile', 'tiles');
        if(replaceWith == 'quartz' || replaceWith == 'purpur', replaceWith = replaceWith + '_block');
        if(block_tags(Block, 'wooden_slabs') == true, replaceWith = replaceWith + '_planks');
        set(Block, replaceWith);
    );
);