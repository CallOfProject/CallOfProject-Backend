#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>

#define H2O_COUNT 10

sem_t h = 0, o_wait = 0;

pthread_mutex_t mutex;

int count = 0;


void hydrogen()
{
	pthread_mutex_lock(&mutex);
	count++;
	if (count % 2 == 0)
	{
		pthread_mutex_unlock(&mutex);
		sem_wait(&h);
	}
	else
	{
		sem_post(&o_wait);
		sem_wait(&h);	
		pthread_mutex_unlock(&mutex);
	}
	printf("Created H\n");	
}


void oxygen()
{
	sem_wait(&o_wait);
	sem_post(&h);
	sem_post(&h);
	printf("Created O\n");
}

int main()
{
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
	return 0;
}