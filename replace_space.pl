#!/usr/bin/perl

############################GESTION DES OPTIONS##################################"

my ($opt_h, $opt_ver);
GetOptions(
           "h"   => \$opt_h, 		"help"  	=> \$opt_h,
           "v"   => \$opt_ver,    	"version"  	=> \$opt_ver,
           "o=s"   => \$opt_oldchar, 	"oldchar=s"  	=> \$opt_oldchar,
           "n=s"   => \$opt_newchar,    "newchar=s"  	=> \$opt_newchar,
);


if ($opt_h) {
    print_help();
    exit;
}

if ($opt_ver) {
        print_version();
        exit;
}

my $old_char;
my $new_char;

if ($opt_oldchar) {
        $old_char =  $opt_oldchar;
}else{
	$old_char = " ";
}

if ($opt_newchar) {
        $new_char =  $opt_newchar;
}else{
	$new_char =  "_";
}


sub print_help {
    print "    Usage: replace_space.pl {-v -h -o \"old_char\" -n \"new_char\" } \n";
    print "    replace_space.pl 1.02
    Copyright (c) 2013 Jason LELORRAIN
    This program is free software; you can redistribute it and/or modify it under the same terms as Perl itself. 

    -h, --help
        Display detailed help
    -v, --version
        Show version information
    -o, --oldchar
        char to replace, by default : \" \"
    -n, --newchar
        new char to place, by default : _
    
    This program will replace all \"oldchar\" (by default space) by \"newchar\" (by default underscore) for all repository name in the sub tree

    !!!warning!!! => the current directory must do not contain special character
       

";

exit 0
}

sub print_version {
        print "
    replace_space.pl  version 1.02 - May 15th, 2013
    Copyright (c) 2013 Jason LELORRAIN 
    This program is free software; you can redistribute it and/or modify it under the same terms as Perl itself. 
";
exit 0
}

############################################################################################################################################
## remplace dans le dossier courant tous les espaces par des "_", le dossier courant ne doit pas lui même posseder de caractere speciaux. ##
############################################################################################################################################
use strict;
use Getopt::Long;

my $list;
my %final_tab;
my $PATH = `pwd`;
my @ListeRep= `find $PATH`;
foreach $list (@ListeRep){

	my $oldlist1 = "$list";
	my @oldlist = split("/", $oldlist1);
	my $nblist = scalar(@oldlist);
	$oldlist[$nblist-1]=~ s/\n//g;
	$oldlist1 =~ s/\n//g;
	my $list2 = $oldlist[$nblist-1];
	my $PATHDIR="";
	for(my $i=0 ; $i<($nblist-1); $i++){
		$oldlist[$i]=~ s/$old_char/$new_char/g;
		$PATHDIR=$PATHDIR.$oldlist[$i]."/"
	}
	$list2=~ s/$old_char/$new_char/g;
	$final_tab{"$oldlist1"} = $PATHDIR.$list2;
	print $oldlist1." , ".$PATHDIR.$list2."\n";
}

print "Appliquer les modifications ci dessus (yes/no) ? : ";
my $resp = <STDIN>;
$resp =~ s/\n//g;
if ( $resp eq 'y' || $resp eq "yes" ) {
	foreach my $k (keys(%final_tab)) {
		#print $k." , ".$final_tab{$k}."\n";
		rename ($k , $final_tab{$k});
	}
}
exit 0;
##########################################################################################################################################
############################################    COPYRIGHT JASON LELORRAIN, 2013     ######################################################
##########################################################################################################################################
