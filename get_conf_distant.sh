#!/bin/bash


#arborescence des fichiers
Pathrepo=etc/
PathrepoSnmp=snmp/
PathrepoSmtp=postfix/
Pathrepoldap=ldap/
Pathnrpe=nagios/

#nom des fichiers
ldap=ldap.conf
ntp=ntp.conf
dns=resolv.conf
snmp=snmpd.conf
smtp=main.cf
nrpe=nrpe.cfg

# Reset
Color_Off='\e[0m'       # Text Reset

# Regular Colors
Black='\e[0;30m'        # Black
Red='\e[0;31m'          # Red
Green='\e[0;32m'        # Green
Yellow='\e[0;33m'       # Yellow
Blue='\e[0;34m'         # Blue
Purple='\e[0;35m'       # Purple
Cyan='\e[0;36m'         # Cyan
White='\e[0;37m'        # White

# Bold
BBlack='\e[1;30m'       # Black
BRed='\e[1;31m'         # Red
BGreen='\e[1;32m'       # Green
BYellow='\e[1;33m'      # Yellow
BBlue='\e[1;34m'        # Blue
BPurple='\e[1;35m'      # Purple
BCyan='\e[1;36m'        # Cyan
BWhite='\e[1;37m'       # White

function color_echo {
    echo -en "$1$2${Color_Off}"
}
function init_substep {
        ERR_STEP_FLAG=0
        COMMENT=""
}
function init_step {
        echo
        echo "> $STEP" | tr a-z A-Z
        init_substep
}

function step_status {
        echo -en "["
        if [ $ERR_STEP_FLAG -eq 0 ]; then
                color_echo $Green "OK"
        else 
                color_echo $Red "NOK"
        fi  
        echo -en "] \t$STEP"
        if [ "$1" != "" ]; then 
                echo -ne " - $1"
        fi  
        echo 
}

echo
GLOBAL_VAR=""
GLOBAL_CONF=""


color_echo $BBlue "#############################################################################################################################"
color_echo $BBlue "##############################           TEST CONFIGURATION       ###########################################################"
color_echo $BBlue "#############################################################################################################################"


#test la version d'OS
os=$(cat /etc/issue)
testOs=$(echo "$os" | grep -iE 'Ubuntu|Debian' && echo ok || echo ko)
if [ "$testOs" != 'ko' ]; then

    > /tmp/tmp.txt
    #echo "----------------------------------conf_LDAP : " >> tmp.txt
    cd /
    cd $Pathrepo
    if [ -f "${Pathrepoldap}$ldap" ];
    then
	   if [ -f "libnss-ldap.conf" ];
	   then
		Package=libnss-ldap.conf
	   else 
		cd $Pathrepoldap
		Package=$ldap
	   fi
	   SUB_VIP_ADM=$( grep 'host ' $Package | sed "s+host ++g")
	   VIP_ADM=$( echo $SUB_VIP_ADM | cut -d "#" -f1)
	   echo "$VIP_ADM" >> /tmp/tmp.txt
    fi
    echo -e " ; " >> /tmp/tmp.txt


    cd /etc/
    if [ -f "$ntp" ];
    then
	    #echo "----------------------------------conf_NTP : " >> ../$HOME/tmp.txt
	    Package=$ntp
	    VIP_NTP=$( grep 'server ' $Package | sed "s+server ++g" | cut -d "#" -f1 )
    	    echo "$VIP_NTP" >> /tmp/tmp.txt
	    echo -e " ; " >> /tmp/tmp.txt
    fi


    if [ -f "$dns" ];
    then
	    #echo "----------------------------------conf_DNS : " >> ../$HOME/tmp.txt
	    Package=$dns
	    VIP_MID=$( grep nameserver $Package | sed "s+nameserver ++g" | sed "s+ ++g" )
	    echo "$VIP_MID" >> /tmp/tmp.txt
	    echo -e " ; " >> /tmp/tmp.txt
    fi


    if [ -d "$PathrepoSnmp" ];
    then
	    #echo "----------------------------------conf_SNMP : " >> ../$HOME/tmp.txt
	    cd $PathrepoSnmp
	    Package=$snmp
	    VIP_SNMP=$(grep 'com2sec readonly  ' $Package | sed "s+com2sec readonly  ++g" | sed "s+public++g")
	    echo "$VIP_SNMP" >> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt
    fi

    if [ -d "$PathrepoSmtp" ];
    then
	    #echo "----------------------------------conf_SMTP : " >> ../$HOME/tmp.txt
	    cd $PathrepoSmtp
	    Package=$smtp
	    VIP_SMTP=$(grep 'relayhost = ' $Package | sed "s+relayhost = ++g") 
	    echo "$VIP_SMTP"	>> /tmp/tmp.txt
	    cd ../	
	    echo -e " ; " >> /tmp/tmp.txt
	    cd $PathrepoSmtp
	    VIP_SMTP2=$(grep 'fallback_relay' $Package | sed "s+fallback_relay = ++g")
	    echo "$VIP_SMTP2"	>> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt
    fi


    if [ -d "$Pathnrpe" ];
    then
	    #echo "----------------------------------conf_NRPE : " >> ../$HOME/tmp.txt
	    cd $Pathnrpe
	    Package=$nrpe
	    grep 'server_address=' $Package | sed "s+server_address=++g" >> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt
    fi

else


    > /tmp/tmp.txt
    #echo "----------------------------------conf_LDAP : " >> tmp.txt
    cd /
    if [ -d "$Pathrepo" ];
    then
	    cd $Pathrepo
	    Package=ldap.conf
	    SUB_VIP_ADM=$( grep 'host ' $Package | sed "s+host ++g")
	    VIP_ADM=$( echo $SUB_VIP_ADM | cut -d "#" -f1)
	    echo "$VIP_ADM" >> /tmp/tmp.txt
	    echo -e " ; " >> /tmp/tmp.txt
    fi

    if [ -f "$ntp" ];
    then
    #echo "----------------------------------conf_NTP : " >> ../$HOME/tmp.txt
    Package=$ntp
    VIP_NTP=$( grep 'server ' $Package | sed "s+server ++g"| cut -d "#" -f1 )
    echo "$VIP_NTP" >> /tmp/tmp.txt
    echo -e " ; " >> /tmp/tmp.txt
    fi

    if [ -f "$dns" ];
    then
	    #echo "----------------------------------conf_DNS : " >> ../$HOME/tmp.txt
	    Package=$dns
	    VIP_MID=$( grep nameserver $Package | sed "s+nameserver ++g" )
	    echo "$VIP_MID" >> /tmp/tmp.txt
	    echo -e " ; " >> /tmp/tmp.txt
    fi


    if [ -d "$PathrepoSnmp" ];
    then
	    #echo "----------------------------------conf_SNMP : " >> ../$HOME/tmp.txt
	    cd $PathrepoSnmp
	    Package=$snmp
	    VIP_SNMP=$(grep 'com2sec' $Package | sed "s+com2sec readonly  ++g" | sed "s+public++g")
	    echo "$VIP_SNMP" >> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt
    fi



    if [ -d "$PathrepoSmtp" ];
    then
	    #echo "----------------------------------conf_SMTP : " >> ../$HOME/tmp.txt
	    cd $PathrepoSmtp
	    Package=$smtp
	    VIP_SMTP=$(grep 'relayhost = ' $Package | sed "s+relayhost = ++g") 
	    echo "$VIP_SMTP"	>> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt
	    cd $PathrepoSmtp
	    VIP_SMTP2=$(grep 'fallback_relay' $Package | sed "s+fallback_relay = ++g")
	    echo "$VIP_SMTP2"	>> /tmp/tmp.txt
	    cd ../
	    echo -e " ; " >> /tmp/tmp.txt	
    fi

    if [ -d "$Pathnrpe" ];
    then
	    #echo "----------------------------------conf_NRPE : " >> ../$HOME/tmp.txt
	    cd $Pathnrpe
	    Package=$nrpe
	    VIP_NRPE=$(grep 'server_address=' $Package | sed "s+server_address=++g")
	    echo "$VIP_NRPE" >> /tmp/tmp.txt
	    echo -e " ; " >> /tmp/tmp.txt
    fi

fi ##fin de boucle if testOs

############################################# COMMON FUNCTIONS ###################################################################

# some functions
help()
{
    cat <<HELP

usage: get_conf_distant {-c -h -r -v  -d [@DNS] -l [@LDAP] -m [@SMTP] -n [@NTP] -p [@NRPE] -s [@SNMP]}
recupere, test et affiche les configurations des services presents sur l'equipements

 -c => delta_conf_puppet
 -d => dns
 -h => Help
 -l => ldap
 -m => smtp
 -n => ntp
 -p => nrpe
 -r => print results (like version 1.01)
 -s => snmp
 -v => Version



HELP
    exit 0
}


function version {
   echo -e  "

$0  version 1.02 - May 6th, 2013
Copyright (c) 2013 Jason LELORRAIN 
This program is free software; you can redistribute it and/or modify it under the same terms as shell itself. 

"
exit 0
}

error()
{
    # display error
    echo -e "\n[ERROR $1]\t$2\n\n----> Please run 'get_conf_distant -h' for the help\n" 2> stderr
    exit 1
}

function result {

echo '------------------------------------------------------------------------------'
echo '-----------------------------------RESULT-------------------------------------'
echo '------------------------------------------------------------------------------'
export tmp="$(cat /tmp/tmp.txt)" 
echo $tmp
echo
rm /tmp/tmp.txt
exit 0

}

function addr_dns {
STEP="CONF DNS $OPTARG"
init_step
#color_echo $Yellow "> DNS CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_MID | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST

}

function addr_ntp {
STEP="CONF NTP $OPTARG"
init_step
#color_echo $Yellow "> NTP CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_NTP | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST

}

function addr_ldap {
STEP="CONF LDAP $OPTARG"
init_step
#color_echo $Yellow "> LDAP CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_ADM | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST
}

function addr_smtp {
STEP="CONF SMTP $OPTARG"
init_step
#color_echo $Yellow "> SMTP CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_SMTP | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST

}

function addr_snmp {
STEP="CONF SNMP $OPTARG"
init_step
#color_echo $Yellow "> SMTP CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_SNMP | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST

}

function addr_nrpe {
STEP="CONF NRPE $OPTARG"
init_step
#color_echo $Yellow "> SMTP CONFIGURATION STATE $OPTARG"
TEST=$( echo $VIP_NRPE | grep $OPTARG && echo "OK" || echo "NOK")

test_conf $TEST

}

function test_conf {

if [ "$1" != "NOK" ] ;then

	color_echo $Green '[OK]'
	GLOBAL_CONF=${GLOBAL_CONF}"OK"
else
	color_echo $Red '[NOK]' 
	GLOBAL_CONF=${GLOBAL_CONF}"NOK"
fi
echo

}

function delta_conf_puppet {
#test du delta entre la conf puppet distante et la local
STEP="DELTA CONF PUPPET"
init_step

cd /tmp/
puppetd -tv --noop |grep "should be" > /dev/null
testdeltapuppet=$(echo $?)
if [ "$testdeltapuppet" != 0 ]; then
	
	color_echo $Green '[OK] - pas de delta'

else 
	color_echo $Red '[NOK] - difference entre la conf puppet distante et la conf puppet local'

fi 
echo
echo

rm /tmp/tmp.txt
exit 0

}


while getopts ":d:n:l:m:s:p:hvrc" OPTION
do
    case $OPTION in
    h) help;;
    v) version;;
    r) result;;
    d) addr_dns;;
    n) addr_ntp;;
    m) addr_smtp;;
    l) addr_ldap;;
    s) addr_snmp;;
    p) addr_nrpe;;
    c) delta_conf_puppet;;
    :) error 1 "Option '-$OPTION' needs an argument."
       exit 1;;
    *) error 2 "Unrecognized option '-$OPTION'."
       exit 1;;
    esac
done


###################################################### CONF PUPPET ################################################################################ 
#recup de la conf puppet

STEP="CONF PUPPET"
init_step

cd /etc/puppet
puppet_run=$(grep 'rundir' puppet.conf | sed "s+rundir=++g" | sed 's+ ++g')
puppet_class=$(grep 'classfile' puppet.conf | sed "s+classfile =++g" | sed 's+ ++g')
puppet_server=$(grep 'server' puppet.conf | sed "s+server=++g" | sed 's+ ++g')

if [ "$puppet_run" != "" ]
then
	color_echo $Yellow "RUNDIR : $puppet_run"
	echo
fi

if [ "$puppet_class" != "" ]
then
	color_echo $Yellow "CLASSFILE : $puppet_class"
	echo
fi

if [ "$puppet_server" != "" ]
then
	color_echo $Yellow "SERVER : $puppet_server"
	echo
fi


######################################################### GLOBAL CONF STATE #####################################################################

STEP="GLOBAL CONF STATE"
init_step
echo $GLOBAL_CONF | grep -q 'N'  &&  color_echo $Red '[NOK]' || color_echo $Green '[OK]'
echo 
echo
echo
color_echo $BBlue "#############################################################################################################################"
color_echo $BBlue "##############################           TEST SERVICE    ####################################################################"
color_echo $BBlue "#############################################################################################################################"

################################################# SMTP #########################################################################
# Test du port 25 (SMTP) sur le SVCC
STEP="CHECK SMTP"
init_step
IP_ADM_SVCC_SMTP=$(cat -s /tmp/tmp.txt | sed 's/\n//')
IP_ADM_SVCC_SMTP2=$(echo $IP_ADM_SVCC_SMTP | cut -d ";" -f5,6)

for SMTP4 in ${IP_ADM_SVCC_SMTP2[@]}; do

if [ "$SMTP4" != " " ] && [ "$SMTP4" != ";" ]
then
	SMTP=`nc -z -w 1 -q 1  $SMTP4 25; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`
	GLOBAL_VAR=${GLOBAL_VAR}${SMTP}
	if  [ "$SMTP" == "OK" ]
	then 
		init_substep
		RELAY_IP=`cat /etc/postfix/main.cf | grep "relayhost" | awk '{ print $3 }'`
		nc -w 1 -q 1 -z $RELAY_IP 25
		ERR_STEP_FLAG=$?
		step_status "$SMTP4 Service SMTP (test port 25 + telnet)"
	fi

	if  [ "$SMTP" == "NOK" ]
	then 
		init_substep
		RELAY_IP=`cat /etc/postfix/main.cf | grep "relayhost" | awk '{ print $3 }'`
		nc -w 1 -q 1 -z $RELAY_IP 25
		ERR_STEP_FLAG=$?
		step_status "$SMTP4 Service SMTP (test port 25 + telnet)"
	fi
fi
done



####################################### Resolution DNS  #######################################################################

# Test du port 53 (DNS) sur le SVCC
# Resolution DNS
STEP="CHECK DNS"
init_step
IP_ADM_SVCC_DNS=$(cat -s /tmp/tmp.txt | sed 's/\n//')
IP_ADM_SVCC_DNS2=$(echo $IP_ADM_SVCC_DNS | cut -d ";" -f3 )

for DNS4 in ${IP_ADM_SVCC_DNS2[@]}; do

DNS=`nc -z -w 1 -q 1  $DNS4 53; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`
#echo $DNS

GLOBAL_VAR=${GLOBAL_VAR}${DNS}

if  [ "$DNS" == "OK" ]
then


### Check Configuration
	for RESOLV in ${DNS_RESOLVERS[@]}; do
	cat /etc/resolv.conf | egrep "nameserver.*$RESOLV" > /dev/null
	 RETURN_CODE=$?
	 COMMENT="$COMMENT $RESOLV"
        if [ $RETURN_CODE -ne 0 ]; then
        ERR_STEP_FLAG=1
              COMMENT="$COMMENT `color_echo $Purple '(manquant)'`"
        fi
        COMMENT="$COMMENT,"
done
if [ "$COMMENT" != "" ]; then
        COMMENT="Resolver(s) $COMMENT"
fi
step_status "$DNS4 $COMMENT(test port 53)"
## Test de rÃ©solution
init_substep
dig +short $NAME_TO_RESOLVE > /dev/null
if [ $? -ne 0 ]; then
        ERR_STEP_FLAG=1
fi
fi
step_status "$DNS4 (Resolution IP)"

#Test de concordance avec NSLOOKUP 
ADDR=$( nslookup $HOSTNAME $DNS4 )
rep_IP=$(echo "$ADDR" | grep 'Address:' | grep -v '#' | sed 's+Address:++g' | sed 's+ ++g' | sed 's+\n++g')
DNS_IP=$(echo "$ADDR" | grep 'Server:' | sed 's+Server:++g' | sed 's+\t++g')
if [ "$DNS_IP" == "$DNS4" ] && [ $rep_IP ]; then
	    HOST=$( nslookup "$rep_IP" | grep "name" | cut -d '=' -f2 | sed 's+ ++g')
	    short_HOST1=$(echo $HOSTNAME | cut -d '-' -f1)
	    short_HOST2=$(echo $HOSTNAME | cut -d '-' -f2)
	    HOST3="${short_HOST1}-${short_HOST2}"
	    testnslookup=$(echo $HOST | grep '$HOST3' && echo ko || echo ok)
	    if [[ $HOST == *$HOST3* ]]; then
		      NEWCOMMENT="`color_echo $Green   'OK'`"
		      NEWCOMMENT="[${NEWCOMMENT}]     CHECK SERVICE DNS - $DNS4 (comparaison NAME/IP puis IP/NAME)"
		      echo $NEWCOMMENT
		      echo
	    else
		      NEWCOMMENT="`color_echo $Red  'NOK'`" 
		      NEWCOMMENT="[${NEWCOMMENT}]     CHECK SERVICE DNS - $DNS4 (comparaison NAME/IP puis IP/NAME)"
		      echo $NEWCOMMENT
		      echo
	    fi
else
	    NEWCOMMENT="`color_echo $Red  'NOK'`" 
	    NEWCOMMENT="[${NEWCOMMENT}]     CHECK SERVICE DNS - $DNS4 (comparaison NAME/IP puis IP/NAME)"
	    echo $NEWCOMMENT
	    echo
fi

done

#test la presence de 'dig'
testpresencedig=$(which dig | grep -qiE 'dig' && echo ok || echo ko )
if [ "$testpresencedig" != 'ko' ]; then
	# test DNS #
	ADDR=$(dig www.sfr.fr)
	testDNS=$(echo "$ADDR" | grep -qiE ';; Query time: 0 msec|;; connection timed out' && echo ko || echo ok)
	if [ "$testDNS" != 'ko' ]; then
	    NEWCOMMENT="`color_echo $Green   'OK'`"
	    NEWCOMMENT="[${NEWCOMMENT}]     Resolution de \"www.sfr.fr\" (=> dig)"
	    echo $NEWCOMMENT
	else
	    NEWCOMMENT="`color_echo $Red  'NOK'`" 
	    NEWCOMMENT="[${NEWCOMMENT}]     Resolution de \"www.sfr.fr\" (=> dig)"
	    echo $NEWCOMMENT

	fi ##fin de boucle if testdns

else 
	ADDR=$(nslookup  www.sfr.fr)
	testDNS=$(echo "$ADDR" | grep -qiE 'server can\047t find |;; connection timed out' && echo ko || echo ok)
	if [ "$testDNS" != 'ko' ]; then
		    NEWCOMMENT="`color_echo $Green   'OK'`" 
		    NEWCOMMENT="[${NEWCOMMENT}]     Resolution de \"www.sfr.fr\" (=> nslookup)"
		    echo $NEWCOMMENT
		else
		    NEWCOMMENT="`color_echo $Red  'NOK'`" 
		    NEWCOMMENT="[${NEWCOMMENT}]     Resolution de \"www.sfr.fr\" (=> nslookup)"
		    echo $NEWCOMMENT

		fi ##fin de boucle if testdns



fi ##fin de boucle testpresencedig

#
########################################## NTP ###################################################################################
#
# Test du port 123 (NTP) sur le SVCC
STEP="CHECK NTP"
init_step
IP_ADM_SVCC_NTP=$(cat -s /tmp/tmp.txt | sed 's/\n//')
IP_ADM_SVCC_NTP2=$(echo $IP_ADM_SVCC_NTP | cut -d ";" -f2 )

for NTP4 in ${IP_ADM_SVCC_NTP2[@]}; do

NTP=`nc -zu -w 1 -q 1 $NTP4 123; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`
GLOBAL_VAR=${GLOBAL_VAR}${NTP}
#echo $NTP1
#echo $NTP
if  [ "$NTP" == "OK" ]
then
### Test du service NTP
# NTP

## Check Configuration
for NTP in ${NTP_SRV[@]}; do
        cat /etc/ntp.conf | egrep "server.*$NTP" | grep -v "#" > /dev/null
        RETURN_CODE=$?
        COMMENT="$COMMENT $NTP"
        if [ $RETURN_CODE -ne 0 ]; then
        ERR_STEP_FLAG=1
                COMMENT="$COMMENT `color_echo $Purple '(manquant)'`"
    fi
        COMMENT="$COMMENT,"
done
if [ "$COMMENT" != "" ]; then
        COMMENT="Serveur(s) $COMMENT"
fi
step_status "$NTP4 $COMMENT (test port 123)"


for NTP in ${NTP_SRV[@]}; do
        if [ "`ntpdate -q 127.0.0.1 | grep "$NTP" | awk '{ print $3 }'`" == "16" ] ; then
	        ERR_STEP_FLAG=1
		COMMENT="$COMMENT `color_echo $Purple '(manquant)'`"
		    fi
		    COMMENT="$COMMENT,"
done
if [ "$COMMENT" != "" ]; then
	COMMENT="$COMMENT,"
fi
else

step_status "$NTP4 $COMMENT (test port 123)"

fi

done

###################################################### LDAP ################################################################################ 
# Test du port 389 (LDAP) vers le SVCC
# Le test ci-dessous va checker si la commande gentent sur le user courant repond bien 
STEP="CHECK LDAP"
init_step

IP_ADM_SVCC_LDAP=$(cat -s /tmp/tmp.txt | sed 's/\n//')
IP_ADM_SVCC_LDAP2=$(echo $IP_ADM_SVCC_LDAP | cut -d ";" -f1 )

for LDAP4 in ${IP_ADM_SVCC_LDAP2[@]}; do

LDAP=`nc -z -w 1 -q 1  $LDAP4 389 ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`
GLOBAL_VAR=${GLOBAL_VAR}${LDAP}
#echo $LDAP
init_substep
getent passwd support_tech > /dev/null
if [ $? != 0 ]
then
ERR_STEP_FLAG=2
COMMENT="$COMMENT `color_echo $Purple 'USER \"'support_tech'\" NON TROUVE' `"
COMMENT="$COMMENT `color_echo $Purple 'Verifier la config LDAP' `"
fi
if [ "$COMMENT" != "" ]; then
        COMMENT="$COMMENT"
fi
step_status "$LDAP4 $COMMENT (test port 389 + test du compte \"support_tech\")"

done

###################################################### CHECK PUPPETd ################################################################################ 
#test la precense de la cmd puppetd
STEP="CHECK PUPPETd"
init_step

cd /tmp/
testpresencepuppetd=$(which puppetd | grep -qiE 'puppetd' && echo ok || echo ko )
if [ "$testpresencepuppetd" != 'ko' ]; then
	NEWCOM="`color_echo $Green   'OK'`"
	NEWCOM="[${NEWCOM}]   - presence de la commande \"puppetd\""
	echo "$NEWCOM"
else 
	NEWCOM="`color_echo $Red  'NOK'`" 
	NEWCOM="[${NEWCOM}]   - absence de la commande \"puppetd\""
	echo "$NEWCOM"
fi ##fin de boucle testpresencepuppetd
echo
#####################################################  Route #################################################################################

# Liste des routes
STEP="ROUTES"
init_step
ROUTES=`netstat -rn | tail -n+3 | sort -k8`

init_substep
netstat -rn | tail -n+3 | sort -k8


if [ $? != 0 ]
then
ERR_STEP_FLAG=2
COMMENT="$COMMENT `color_echo $Purple 'PAS DE ROUTES' `"
fi

######################################################### GLOBAL SERVICE STATE #####################################################################
echo 
echo
STEP="GLOBAL SERVICE STATE"
init_step
echo $GLOBAL_VAR | grep -q 'N'  &&  color_echo $Red '[NOK]' || color_echo $Green '[OK]'
echo 
echo

################################################################ RESULT #############################################################################

#wget proxy 31.28


rm /tmp/tmp.txt
exit 0


