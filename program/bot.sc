// bot manager by RS256

global_app_name = system_info('app_name');
global_scriptVersion = '0.0.0';
global_author = 'RS256';
global_defaultDirectory = 'bot/';

_error(error) -> exit(print(format(str('r %s', error))));

__config() -> {
	'commands' ->{
		'' -> 'test',
		'config create <id> <mcid> <forcible> <command>' -> 'createConfig',
		'config delete <existing_id>' -> 'deleteConfig',
		'config info' -> 'infoConfig',
		'config info <existing_id>' -> 'infoSpecificConfig',
		'config modify <existing_id> insert <index> <command>' ->'modifyConfigInsert',
		'config modify <existing_id> append <command>' -> 'modifyConfigAppend',
		'config modify <existing_id> prepend <command>' -> 'modifyConfigPrepend',
		'config modify <existing_id> remove index <index>' -> 'modifyConfigRemoveByIndex',
		'config modify <existing_id> remove all' -> 'modifyConfigRemoveAll',
		'execute <existing_id>' -> 'executeScript',
		'query <fake_player>' -> 'queryPlayer'
	},
	'arguments' -> {
		'id' -> {
			'type' -> 'term',
			'suggest' -> []
		},
		'mcid' -> {
			'type' -> 'term',
			'suggest' -> []
		},
		'index' -> {
			'type' -> 'int',
			'suggest' -> []
		},
		'existing_id' -> {
			'type' -> 'term',
			'suggester' -> _(args) -> map(list_files('bot', 'text'), slice(_, length('bot') + 1))
		},
		'forcible' -> {
			'type' -> 'bool'
		},
		'command' -> {
			'type' -> 'text',
			'suggest' -> [
				'spawn at 0 0 0 facing 0 0 in minecraft:overworld in survival'
			]
		},
		'fake_player' -> {
			'type' -> 'players'
		}
	}
};

test() -> (
	texts = 'version : ' + global_scriptVersion + '\nauthor : ' + global_author;
	print(texts);
);

createConfig(id, mcid, forcible, command) -> (
	if (hasFile(global_defaultDirectory, id) == true, _error('there is already script with that id'));
	if (forcible == true && player()~'permission_level' <= 2, _error(str('You cannot create forcible:true at your permission level : %s, available with level 2 and above', player()~'permission_level')));
	write_file(str('bot/%s', id), 'text', mcid, forcible, command);
	print(format('f » ', 'g Successfully created the ', str('#FFEE44 %s', id)));
);

deleteConfig(existing_id) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	delete_file(str('bot/%s', existing_id), 'text');
    print(format('f » ', 'g Successfully deleted the ', str('#FFEE44 %s', existing_id)));
);

infoConfig() ->(
	files = list_files('bot', 'text');
	if(files == null, _error('there is no script available'));
	filelist = map(files, slice(_, length('bot') + 1));
    texts = reduce(filelist, [..._a, if(_i == 0, '', 'g , '), str('#FFEE44 %s', _)], ['f » ', 'g Available scripts : ']);
    print(format(texts));
);

infoSpecificConfig(existing_id) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	file = read_file(str('%s%s', global_defaultDirectory, existing_id), 'text');
	mcid = file:0;
	forcible = file:1;
	c_for(i = 2, has(file:i)==true, i=i+1,
		if(command != null, command = command +'\n' + file:i, command == null, command = file:i)
	);
    print(format(str('#FFEE44 %s', existing_id), 'g  has these data : \nmcid : ', str('#FFEE44 %s', file:0), 'g \nforcible : ', if(forcible == false, str('#DC143C %s', file:1), forcible == true, str('#3CB371 %s', file:1)), 'g \ncommand : ', str('#F0F8FF %s', command)));
);

modifyConfigInsert(existing_id, index, command) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	put(file, index + 2, command, 'insert');
	delete_file(filename, 'text');
	write_file(filename, 'text', file);
	print(format('g command: ',str('w %s ', command), 'g has been inserted in ', str('#FFEE44 %s ', index), 'g of ', str('#FFEE44 %s', existing_id)));
);

modifyConfigAppend(existing_id, command) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	put(file, null, command, 'extend');
	delete_file(filename, 'text');
	write_file(filename, 'text', file);
	print(format('g command: ',str('w %s ', command), 'g has been appended to ', str('#FFEE44 %s', existing_id)));
);

modifyConfigPrepend(existing_id, command) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	put(file, 2, command, 'extend');
	delete_file(filename, 'text');
	write_file(filename, 'text', file);
	print(format('g command: ',str('w %s ', command), 'g has been prepended to ', str('#FFEE44 %s', existing_id)));
);

modifyConfigRemoveByIndex(existing_id, index) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	delete(file:(index+1));
	delete_file(filename, 'text');
	write_file(filename, 'text', file);
	print(format('g line ',str('w %s ', index), 'g has been removed from ', str('#FFEE44 %s', existing_id)));
);

modifyConfigRemoveAll(existing_id) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	c_for(i = 2, has(file:2) == true, i = i + 1,
		delete(file, 2);
	);
	delete_file(filename, 'text');
	write_file(filename, 'text', file);
	print(format('g all command has been removed from ', str('#FFEE44 %s', existing_id)));
);

// execute script

executeScript(existing_id) -> (
	if (hasFile(global_defaultDirectory, existing_id) == false, _error(str('%s doesn\'t exist', existing_id)));
	filename = global_defaultDirectory + existing_id;
	file = read_file(filename, 'text');
	mcid = file:0;
	forcible = file:1;
	if (forcible == true && player()~'permission_level' <= 2, _error(str('You cannot execute forcible:true at your permission level : %s, available with level 2 and above', player()~'permission_level')));
	if(player(mcid) != null, errorText = format('w mcid: ', str('b %s ', mcid), 'w is already logged on'); _error(errorText));
	if (forcible == true, 
		print(format(str('#FFA500 forcible command is executed by %s', player())));
		logger('warn', str('forcible command is executed by %s', player()));
	);
	c_for(i = 2, has(file:i) == true, i = i + 1,
		command = file:i;
		if (command != null,
			command =  'player ' + mcid + ' ' + command;
			tester = replace(command, '(in\\s(adventure|creative|survival|spectator))', 'in ' + player()~'gamemode');
			if (forcible  == false && tester != command, _error(str('you must be same gamemode as %s', mcid)));
			runCommand(command, mcid, forcible);
		);
	);
);

runCommand(command, mcid, forcible) -> (
	logger('warn', command);
	run(command);
	print(format('g executed command: ', str('w %s',command)));
);

// system

hasFile(directory, filename) -> (
	filename = str('%s%s',directory , filename);
	return(list_files(directory, 'text')~filename != null);
);

// query

queryPlayer(fake_player) -> (
	if (player(str('%s', fake_player))~'player_type' != 'fake', _error(str('player : %s is not fake player', fake_player)));
	if (player(str('%s', fake_player))~'player_type' == 'fake',
		coordinate = player(str('%s', fake_player))~'pos';
		dimension = player(str('%s', fake_player))~'dimension';
		print(format(str('#FFEE44 %s ', fake_player),'g is at ' , str('w (%d, %d, %d) ',coordinate), 'g in ', str('w %s', dimension)));
	);
);