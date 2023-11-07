#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdlib.h>


#define H2O_COUNT 8


typedef struct H2O
{
	int id;
	int h_count;
	int o_count;
} *H2O;

typedef struct NODE
{
	void *data;
	struct NODE *next;
} *Node;

typedef struct LINKED_LIST
{
	Node head;
}*List;



//---------------------------------GLOBAL AREA---------------------------------

sem_t h = 0, o_wait = 0;

pthread_mutex_t mutex;

static int id = 0;
int count = 0;

List list;

//---------------------------------GLOBAL AREA---------------------------------



//--------------------------------------LIST-----------------------------------
List createList()
{
	List list = malloc(sizeof(struct LINKED_LIST));
	list->head = NULL;
	
	
	return list;
}
Node createNode(void *data)
{
	Node node = malloc(sizeof(struct NODE));

	node->data = data;
	node->next = NULL;

	return node;
}


int isEmpty(List list)
{
	return list->head == NULL ? -1 : 0;
}


int insertLast(List list, void *data)
{
	Node node = createNode(data);
	
	if (isEmpty(list) == -1)
	{
		list->head = node;
		return 0;
	}
	
	else
	{
		Node p = list->head;
		
		for (; p->next != NULL; p = p->next);
		
		p->next = node;
		
		return 0;
	}	
	
	return -1;
	
}

void printList(List list, void(*printFunc)(void *))
{
	Node node = list->head;
	
	for (; node != NULL; node = node->next)
		printFunc(node->data);
}
//--------------------------------------LIST-----------------------------------



//--------------------------WATER-----------------------------
H2O createWater()
{
	H2O water = malloc(sizeof(struct H2O *));

	water->id = ++id; 
	water->h_count = 0;
	water->o_count = 0;

	return water;
}

void makeWater(H2O water)
{
	water->h_count = 2;
	++water->o_count;
}

void printWater(H2O water)
{
	printf("\n----------------H20------------------\n");
	printf("Water ID: %d \n", water->id);
	printf("H: %d \n", water->h_count);
	printf("O: %d \n", water->o_count);
	printf("----------------H20------------------\n\n");
}

//--------------------------WATER-----------------------------






void hydrogen()
{
	for (int i = 0; i < H2O_COUNT; ++i)
	{	
		
		if (count == 2)
		{
			sem_wait(&h);		
			sem_post(&o_wait);
		}
		count++;
	}
	
	pthread_mutex_unlock(&mutex);
}




void oxygen()
{
	pthread_mutex_lock(&mutex);
	for (int i = 0; i < H2O_COUNT; ++i)
	{
		
		if (count < 2)
			sem_wait(&o_wait);	
		
		
		
		H2O water = createWater();
		makeWater(water);
		
		Sleep(360 + rand() % 450);	
		
		if (count == 2)
		{
			insertLast(list, water);
			count = 0;
			sem_post(&h);	
		}
		
			
	}
	pthread_mutex_unlock(&mutex);
	
}



int main()
{
	list = createList();

	srand(time(NULL));
	pthread_t hydrogen1, hydrogen2, o2;
	
	pthread_mutex_init(&mutex, NULL);
	
	sem_init(&h, 0, 0);
	sem_init(&o_wait, 0, 0);
	
	pthread_create(&hydrogen1, NULL, hydrogen, NULL);
	pthread_create(&hydrogen2, NULL, hydrogen, NULL);
	pthread_create(&o2, NULL, oxygen, NULL);
	
	pthread_join(hydrogen1, NULL);
	pthread_join(hydrogen2, NULL);
	pthread_join(o2, NULL);
	
	pthread_mutex_destroy(&mutex);
	
	sem_destroy(&h);
	sem_destroy(&o_wait);

	printList(list, printWater);

	return 0;
}