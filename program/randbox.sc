global_app_name = system_info('app_name');
global_scriptVersion = '1.0.0';
global_author = 'RS256';
global_defaultDirectory = global_app_name + '/';

global_help_page = [
	'/%appname%'
];

global_boxPosList = [];

_error(error) -> exit(print(format(str('r %s', error))));

__config() -> {
	'commands' ->{
		'' -> 'credit',
		'refill <targetFrom> <targetTo> <from> <to> <targetBlock>' -> 'refill',
		'give <players> <from> <to> <count>' -> ['give', false],
		'give <players> <from> <to> <count> <isBarrel>' -> 'give',
		'fill <targetFrom> <targetTo> <from> <to>' -> ['fill', false],
		'fill <targetFrom> <targetTo> <from> <to> <isBarrel>' -> 'fill'
	},
	'arguments' -> {
		'targetBlock' -> {
			'type' -> 'block'
		},
		'players' -> {
			'type' -> 'players'
		},
		'pos' -> {
			'type' -> 'pos'
		},
		'from' -> {
			'type' -> 'pos'
		},
		'to' -> {
			'type' -> 'pos'
		},
		'targetFrom' -> {
			'type' -> 'pos'
		},
		'targetTo' -> {
			'type' -> 'pos'
		},
		'count' -> {
			'type' -> 'int',
			'min' -> 1,
			'suggest' -> [
				'1',
				'9',
				'27',
				'54'
			]
		},
		'isBarrel' -> {
			'type' -> 'bool'
		}
	}
};

credit() -> (
	texts = 'version : ' + global_scriptVersion + '\nauthor : ' + global_author;
	print(texts);
);

refill(targetFrom, targetTo, from, to, block) -> (
	getAllBoxes(from, to);
	volume(targetFrom, targetTo,
		if(_ == block, 
			blockPos = [_x, _y, _z];
			fillContainerWithBoxes(blockPos);
		);
	);
);

give(players, from, to, count, isBarrel) -> (
	getAllBoxes(from, to);
	if(isBarrel == true,
		for(players,
			giveBarrel(_, count);
		);,
		for(players,
			giveBox(_, count);
		);	
	);
);

fill(targetFrom, targetTo, from, to, isBarrel) -> (
	getAllBoxes(from, to);
	if(isBarrel == true,
		fillBarrel(targetFrom, targetTo);,
	
		fillBox(targetFrom, targetTo);
	);	
);

fillBarrel(targetFrom, targetTo) -> (
	volume(targetFrom, targetTo, 
		set([_x, _y, _z], block('barrel[facing=up]'));
		fillBarrelWithBoxes([_x, _y, _z]);
	);
);

fillBox(targetFrom, targetTo) -> (
	volume(targetFrom, targetTo,
		boxPos = getRandomBoxInList();
		set([_x, _y, _z], block(boxPos), block_data(boxPos));
	);
);

giveBox(player, count) -> (
	loop(count,
		slot = inventory_find(player, null);
		if(slot != null && slot != 36,
			boxPos = getRandomBoxInList();
			tag = '{BlockEntityTag:' + block_data(boxPos) + '}';
			inventory_set(player, slot, 1, block(boxPos), tag);
		);
	);
);

giveBarrel(player, count) -> (
	loop(count,
		slot = inventory_find(player, null);
		if(slot >= 36,
			break
		);
		
		if(slot != null && slot != 36,
			tag = '{BlockEntityTag:{Items:[';
			slot = inventory_find(player, null);
			loop(27,
				boxPos = getRandomBoxInList();
				boxTag = '{Slot: ' 
					+ _
					+ 'b, id: "minecraft:'
					+ str(block(boxPos))
					+ '", Count: 1b, tag: {BlockEntityTag: '
					+ block_data(boxPos)
					+ '}}';
				if(_ != 26, boxTag += ',');
					
				tag += boxTag;
			);
			tag += '],id:"minecraft:barrel"}}';
			
			inventory_set(player, slot, 1, block('barrel'), tag);
		);
	);

);

fillContainerWithBoxes(containerPos) -> (
	loop(inventory_size(containerPos),
		boxPos = getRandomBoxInList();
		tag = '{BlockEntityTag:' + block_data(boxPos) + ',id:"minecraft:shulker_box"}';
		slot = inventory_find(containerPos, null);
		inventory_set(containerPos, slot, 1, block(boxPos), tag);
	);
);

getAllBoxes(from, to) -> (
	global_boxPosList = [];
	volume(from, to, 
		if(block_tags(_, 'shulker_boxes') == true,
			put(global_boxPosList, null, [_x, _y, _z]);
		);
	);
);

getRandomBoxInList() -> (
	r = randInt(length(global_boxPosList));
	return(global_boxPosList:r);
);

randInt(randRange) -> (
	return(floor(rand(randRange)));
);