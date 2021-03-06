%| ?- g_assign(cpt,0), productline(_), g_inc(cpt), fail;g_read(cpt,N). Instead of | ?- findall(L, productline(L), LSol), length(LSol, NbSolutions).

%N = 3225

%(46 ms) yes

productline(L):-

fd_set_vector_max(2000),

L = [UNIX1, Kernel1, Scheduling1, ExecutingInstructions1, InterpretingInstructions1, AccomplishingTheTransferOfData1, AllocatingTheMachinesMemory1, Shell1, FileSystem1, UserInterface1, Graphical1, Process1, Process2, Process3, Process4, Process5, Support_usb1, Cdrom_atech1, Pcmacia_support1, WidthResolution, HeightResolution],
fd_domain([WidthResolution], [0, 800, 1024, 1366]),
fd_domain([HeightResolution], [0, 600, 768]),
fd_domain([UNIX1, Kernel1, Scheduling1, ExecutingInstructions1, InterpretingInstructions1, AccomplishingTheTransferOfData1, AllocatingTheMachinesMemory1, Shell1, FileSystem1, UserInterface1, Graphical1, Process1, Process2, Process3, Process4, Process5], 0, 1),

fd_domain([Support_usb1, Cdrom_atech1, Pcmacia_support1], [0, 1, 2]),
fd_domain([BoolSupport_usb, BoolCdrom_atech, BoolPcmacia_support], [0, 1]),

%Process is not necessary because is taken as a variation point (auxiliar variable) not appearing in the configurations

UNIX1 #= Kernel1,
Kernel1 #= AllocatingTheMachinesMemory1,
AllocatingTheMachinesMemory1 #==> Process,
Kernel1 #= Scheduling1,
Scheduling1 #==> Process,
Kernel1 #= AccomplishingTheTransferOfData1,
AccomplishingTheTransferOfData1 #==> Process,
Shell1 #==>InterpretingInstructions1,
Kernel1 #= InterpretingInstructions1,
Shell1 #==> ExecutingInstructions1,
Kernel1 #= ExecutingInstructions1,

(Support_usb1 #> 0) #<=> BoolSupport_usb,
(Cdrom_atech1 #> 0) #<=> BoolCdrom_atech,
(Pcmacia_support1 #> 0) #<=> BoolPcmacia_support,
0 #=< BoolSupport_usb + BoolCdrom_atech + BoolPcmacia_support ,
BoolSupport_usb + BoolCdrom_atech + BoolPcmacia_support #=< 3*Kernel1,

UNIX1 #= Shell1,
UNIX1 #= FileSystem1,

UNIX1 #>= UserInterface1,
UserInterface1 #= Graphical1,
Graphical1 #= 1 #<=> (WidthResolution #= W1 #/\ HeightResolution #= H1),
Graphical1 #= 0 #<=> (WidthResolution #= 0 #/\  HeightResolution #= 0),
fd_relation([[800, 600], [1024, 768], [1366, 768]], [W1,H1]),

UNIX1 #>= Process,
R1 #= Process1 + Process2 + Process3 + Process4 + Process5,
Process * 1 #=< R1,
R1 #=< Process * 5,

%fd_labelingff(L).
fd_labeling(L,[variable_method(random),value_method(random)]).