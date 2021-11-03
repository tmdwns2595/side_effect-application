from __future__ import division
from __future__ import print_function # __future__모듈을 사용하여 python2에서 python3처럼 작동을 하도록 환경을 설정해준다.
from operator import itemgetter # 튜플의 리스트를 생성해서 정렬을 한다.
from itertools import combinations # 조합에 사용된다. 중복되는 아이템들을 제거한다.
import time
import os
import sys
import pandas as pd
sys.path.append("/home/pslab20/Desktop/decagon-master/")

from decagon.deep.optimizer import DecagonOptimizer
from decagon.deep.model import DecagonModel
from decagon.deep.minibatch import EdgeMinibatchIterator
from decagon.utility import rank_metrics, preprocessing

import tensorflow as tf
import numpy as np
import networkx as nx
import scipy.sparse as sp
from sklearn import metrics
from collections import defaultdict


# Train on CPU (hide GPU) due to memory constraints
os.environ['CUDA_VISIBLE_DEVICES'] = ""

# Train on GPU
# os.environ["CUDA_DEVICE_ORDER"] = 'PCI_BUS_ID'
# os.environ["CUDA_VISIBLE_DEVICES"] = '0'
# config = tf.ConfigProto()
# config.gpu_options.allow_growth = True

np.random.seed(0) # 처음에 랜덤하게 출력된 결과가 똑같이 계속 나온다.

###########################################################
#
# Functions
#
###########################################################


def get_accuracy_scores(edges_pos, edges_neg, edge_type):
    feed_dict.update({placeholders['dropout']: 0})
    feed_dict.update({placeholders['batch_edge_type_idx']: minibatch.edge_type2idx[edge_type]})
    feed_dict.update({placeholders['batch_row_edge_type']: edge_type[0]})
    feed_dict.update({placeholders['batch_col_edge_type']: edge_type[1]})
    rec = sess.run(opt.predictions, feed_dict=feed_dict)

    def sigmoid(x):
        return 1. / (1 + np.exp(-x)) #np.exp(-x) = e^(-x)

    # Predict on test set of edges
    preds = []
    actual = []
    predicted = []
    predicted_pos = []
    predicted_neg = []
    gene1 = []
    gene2 = []
    t_dict, t_dict_val = load_total_dict()
    stitch_dict, gene_dict, node1, target = load_dictionary()
        
    edge_ind = 0
    for u, v in edges_pos[edge_type[:2]][edge_type[2]]:
        print(u,v)
        score = sigmoid(rec[u, v])
        preds.append(score)
        assert adj_mats_orig[edge_type[:2]][edge_type[2]][u,v] == 1, 'Problem 1'

        actual.append(edge_ind)
        predicted.append((score, edge_ind))
        if(target.get(t_dict_val.get(u)) == None or target.get(t_dict_val.get(v)) == None):
        	continue
        gene1 += target[t_dict_val[u]]
        gene2 += target[t_dict_val[v]]
        predicted_pos.append(([target[t_dict_val[u]], target[t_dict_val[v]]], [edge_ind, score]))
        
        edge_ind += 1

    preds_neg = []
    for u, v in edges_neg[edge_type[:2]][edge_type[2]]:
        score = sigmoid(rec[u, v])
        preds_neg.append(score)
        assert adj_mats_orig[edge_type[:2]][edge_type[2]][u,v] == 0, 'Problem 0'

        predicted.append((score, edge_ind))
        if(target.get(t_dict_val.get(u)) == None or target.get(t_dict_val.get(v)) == None):
        	continue
        gene1 += target[t_dict_val[u]]
        gene2 += target[t_dict_val[v]]
        predicted_neg.append(([target[t_dict_val[u]], target[t_dict_val[v]]],[edge_ind, score]))
        
        edge_ind += 1

    preds_all = np.hstack([preds, preds_neg])
    preds_all = np.nan_to_num(preds_all)
    labels_all = np.hstack([np.ones(len(preds)), np.zeros(len(preds_neg))])
    predicted = list(zip(*sorted(predicted, reverse=True, key=itemgetter(0))))[1]
    
    print("---------main의 predicted_pos---------")
    print(predicted_pos)
    print("---------main의 predicted_neg---------")
    print(predicted_neg)
    print("---------main의 preds_all---------")
    print(preds_all)
    print("---------main의 labels_all---------")
    print(labels_all)
    print("---------main의 itemgetter(0)---------")
    print(itemgetter(0))
    print("---------main의 predicted---------")
    print(predicted)
    print("---------main의 predicted 개수---------")
    print(len(predicted))

    roc_sc = metrics.roc_auc_score(labels_all, preds_all)
    aupr_sc = metrics.average_precision_score(labels_all, preds_all)
    apk_sc = rank_metrics.apk(actual, predicted, k=50)
    
    return roc_sc, aupr_sc, apk_sc


def construct_placeholders(edge_types):
    placeholders = {
        'batch': tf.placeholder(tf.int32, name='batch'),
        'batch_edge_type_idx': tf.placeholder(tf.int32, shape=(), name='batch_edge_type_idx'),
        'batch_row_edge_type': tf.placeholder(tf.int32, shape=(), name='batch_row_edge_type'),
        'batch_col_edge_type': tf.placeholder(tf.int32, shape=(), name='batch_col_edge_type'),
        'degrees': tf.placeholder(tf.int32),
        'dropout': tf.placeholder_with_default(0., shape=()),
    }
    placeholders.update({
        'adj_mats_%d,%d,%d' % (i, j, k): tf.sparse_placeholder(tf.float32)
        for i, j in edge_types for k in range(edge_types[i,j])})
    placeholders.update({
        'feat_%d' % i: tf.sparse_placeholder(tf.float32)
        for i, _ in edge_types})
    return placeholders

def load_ppi(fname='/home/pslab20/Desktop/decagon-data/decagon-data/proteinprotein.csv'):
    gene_gene_adj = np.zeros((457,457))
    fin = open(fname)
    print('Reading: %s' % fname)
    fin.readline()
    for line in fin:
        gene1, gene2 = line.strip().split(',')
        gene_gene_adj[t_dict[gene1], t_dict[gene2]] = 1
    return gene_gene_adj

def load_dictionary(fname='/home/pslab20/Desktop/decagon-data/decagon-data/koreatarget.csv'):
    fin = open(fname)
    print('Reading: %s' % fname)
    stitch_idx = defaultdict(set)
    gene_idx = defaultdict(set)
    target = {}
    fin.readline()
    stitch_list = []
    gene_list = []
    for line in fin:
        stitch_id, gene = line.strip().split(',')
        target[gene] = stitch_id
        stitch_list += [[stitch_id]]
        gene_list += [[gene]]
    node1 = list(set([u for e in stitch_list for u in e]))
    node2 = list(set([u for e in gene_list for u in e]))
    for i in range(len(node1)):
        stitch_idx[node1[i]] = i
    for j in range(len(node2)):
        gene_idx[node2[j]] = j
    return stitch_idx, gene_idx, node1, target

def load_mat(fname='/home/pslab20/Desktop/decagon-data/decagon-data/koreatarget.csv'):
    stitch2proteins = defaultdict(set)
    edges3 = []
    medi_gene_adj = np.zeros((8546,457))
    fin = open(fname)
    print('Reading: %s' % fname)
    fin.readline()
    for line in fin:
        stitch_id, gene = line.strip().split(',')
        medi_gene_adj[stitch_dict[stitch_id], t_dict[gene]] = 1
    return medi_gene_adj

def load_total_dict(fname='/home/pslab20/Desktop/decagon-data/decagon-data/proteinline.csv'):
    fin = open(fname)
    print ('Reading: %s' % fname)
    fin.readline()
    total_gene = {}
    total_gene_value = {}
    total = []
    for line in fin:
        gene_id = line.strip().split(',')
        total += gene_id
    for i in range(len(total)):
        total_gene[total[i]] = i
        total_gene_value[i] = total[i]
    return total_gene, total_gene_value

###########################################################
#
# Load and preprocess data (This is a dummy toy example!)
#
###########################################################

####
# The following code uses artificially generated and very small networks.
# Expect less than excellent performance as these random networks do not have any interesting structure.
# The purpose of main.py is to show how to use the code!
#
# All preprocessed datasets used in the drug combination study are at: http://snap.stanford.edu/decagon:
# (1) Download datasets from http://snap.stanford.edu/decagon to your local machine.
# (2) Replace dummy toy datasets used here with the actual datasets you just downloaded.
# (3) Train & test the model.
####

val_test_size = 0.05
n_genes = 457
n_drugs = 8546
n_drugdrug_rel_types = 3 
t_dict, t_dict_val = load_total_dict()
print(len(t_dict))

gene_adj = sp.csr_matrix(load_ppi())
gene_degrees = np.array(gene_adj.sum(axis=0)).squeeze() 

stitch_dict, gene_dict, node1, target = load_dictionary()
drug_gene_adj = sp.csr_matrix(load_mat())
gene_drug_adj = np.transpose(drug_gene_adj)

drug_drug_adj_list = []
tmp = np.dot(drug_gene_adj, gene_drug_adj) 
for i in range(n_drugdrug_rel_types): # 0~4까지 반복문을 돈다.
    mat = np.zeros((n_drugs, n_drugs)) # 약물과 약물간의 연결 관계를 저장하고 있는 mat이라는 2차원 배열을 모두 0으로 초기화하는 부분이다.
    for d1, d2 in combinations(node1, 2): # 약물간의 연결이 가능한 모든 경우의 수를 2개씩 뽑아낸다. 
        if tmp[stitch_dict[d1], stitch_dict[d2]] == i + 4: # 만약 i가 0일때 위에서 뽑아낸 d1과 d2가 공유하는 단백질의 수가 4개이면 mat이라는 행렬에 해당 d1,d2와 d2,d1자리에 1로 표시를 한다.       
            mat[stitch_dict[d1], stitch_dict[d2]] = mat[stitch_dict[d2], stitch_dict[d1]] = 1.
    drug_drug_adj_list.append(sp.csr_matrix(mat)) # 약물간의 공유하는 단백질의 수가 4개인 경우를 모두 찾고나면 연결관계를 저장한 mat이라는 2차원 배열을 매트릭스의 인덱스로 압축하여 저장한다.
drug_degrees_list = [np.array(drug_adj.sum(axis=0)).squeeze() for drug_adj in drug_drug_adj_list] # 각 매트릭스를 하나씩 빼와서 각 인덱스별로 연결된 인덱스가 몇개가 있는지 합을 구한후에 배열의 형태로 drug_degree_list에 저장한다.


# data representation
adj_mats_orig = {
    (0, 0): [gene_adj, gene_adj.transpose(copy=True)],
    (0, 1): [gene_drug_adj],
    (1, 0): [drug_gene_adj],
    (1, 1): drug_drug_adj_list + [x.transpose(copy=True) for x in drug_drug_adj_list],
}
degrees = {
    0: [gene_degrees, gene_degrees],
    1: drug_degrees_list + drug_degrees_list,
}

# featureless (genes)
gene_feat = sp.identity(n_genes)
gene_nonzero_feat, gene_num_feat = gene_feat.shape
gene_feat = preprocessing.sparse_to_tuple(gene_feat.tocoo())

# features (drugs)
drug_feat = sp.identity(n_drugs)
drug_nonzero_feat, drug_num_feat = drug_feat.shape
drug_feat = preprocessing.sparse_to_tuple(drug_feat.tocoo())

# data representation
num_feat = {
    0: gene_num_feat,
    1: drug_num_feat,
}
nonzero_feat = {
    0: gene_nonzero_feat,
    1: drug_nonzero_feat,
}
feat = {
    0: gene_feat,
    1: drug_feat,
}

edge_type2dim = {k: [adj.shape for adj in adjs] for k, adjs in adj_mats_orig.items()}
edge_type2decoder = {
    (0, 0): 'bilinear',
    (0, 1): 'bilinear',
    (1, 0): 'bilinear',
    (1, 1): 'dedicom',
}

edge_types = {k: len(v) for k, v in adj_mats_orig.items()}
num_edge_types = sum(edge_types.values())
print("Edge types:", "%d" % num_edge_types)

###########################################################
#
# Settings and placeholders
#
###########################################################

flags = tf.app.flags
FLAGS = flags.FLAGS
flags.DEFINE_string('f', '', 'kernel')
flags.DEFINE_integer('neg_sample_size', 1, 'Negative sample size.')
flags.DEFINE_float('learning_rate', 0.001, 'Initial learning rate.')
flags.DEFINE_integer('epochs', 100, 'Number of epochs to train.')
flags.DEFINE_integer('hidden1', 64, 'Number of units in hidden layer 1.')
flags.DEFINE_integer('hidden2', 32, 'Number of units in hidden layer 2.')
flags.DEFINE_float('weight_decay', 0, 'Weight for L2 loss on embedding matrix.')
flags.DEFINE_float('dropout', 0.1, 'Dropout rate (1 - keep probability).')
flags.DEFINE_float('max_margin', 0.1, 'Max margin parameter in hinge loss')
flags.DEFINE_integer('batch_size', 512, 'minibatch size.')
flags.DEFINE_boolean('bias', True, 'Bias term.')
# Important -- Do not evaluate/print validation performance every iteration as it can take
# substantial amount of time
PRINT_PROGRESS_EVERY = 150

print("Defining placeholders")
placeholders = construct_placeholders(edge_types)

###########################################################
#
# Create minibatch iterator, model and optimizer
#
###########################################################

print("Create minibatch iterator")
minibatch = EdgeMinibatchIterator(
    adj_mats=adj_mats_orig,
    feat=feat,
    edge_types=edge_types,
    batch_size=FLAGS.batch_size,
    val_test_size=val_test_size
)

print("Create model")
model = DecagonModel(
    placeholders=placeholders,
    num_feat=num_feat,
    nonzero_feat=nonzero_feat,
    edge_types=edge_types,
    decoders=edge_type2decoder,
)

print("Create optimizer")
with tf.name_scope('optimizer'):
    opt = DecagonOptimizer(
        embeddings=model.embeddings,
        latent_inters=model.latent_inters,
        latent_varies=model.latent_varies,
        degrees=degrees,
        edge_types=edge_types,
        edge_type2dim=edge_type2dim,
        placeholders=placeholders,
        batch_size=FLAGS.batch_size,
        margin=FLAGS.max_margin
    )

print("Initialize session")
sess = tf.Session()
sess.run(tf.global_variables_initializer())
feed_dict = {}

###########################################################
#
# Train model
#
###########################################################

print("Train model")
for epoch in range(FLAGS.epochs):

    minibatch.shuffle()
    itr = 0
    while not minibatch.end():
        # Construct feed dictionary
        feed_dict = minibatch.next_minibatch_feed_dict(placeholders=placeholders) # feed_dict는 딕셔너리이다.
        feed_dict = minibatch.update_feed_dict(
            feed_dict=feed_dict,
            dropout=FLAGS.dropout,
            placeholders=placeholders)
        

        t = time.time()

        # Training step: run single weight update
        
        outs = sess.run([opt.opt_op, opt.cost, opt.batch_edge_type_idx], feed_dict=feed_dict)
        print("---------opt.opt_op---------epoch{}".format(epoch))
        print(opt.opt_op)
        print("---------opt.cost---------epoch{}".format(epoch))
        print(opt.cost)
        print("---------opt.batch_edge_type_idx---------epoch{}".format(epoch))
        print(opt.batch_edge_type_idx)
        
        optop = outs[0]
        train_cost = outs[1]
        batch_edge_type = outs[2]
        
        print("---------optop---------epoch{}".format(epoch))
        print(optop)
        print("---------train_cost---------epoch{}".format(epoch))
        print(train_cost)
        print("---------batch_edge_type---------epoch{}".format(epoch))
        print(batch_edge_type)

        if itr % PRINT_PROGRESS_EVERY == 0:
            val_auc, val_auprc, val_apk = get_accuracy_scores(
                minibatch.val_edges, minibatch.val_edges_false,
                minibatch.idx2edge_type[minibatch.current_edge_type_idx])
                
            print("---------minibatch.val_edges---------epoch{}".format(epoch))
            print(minibatch.val_edges)
            print("---------minibatch.val_edges_false---------epoch{}".format(epoch))
            print(minibatch.val_edges_false)
            print("---------minibatch.idx2edge_type[minibatch.current_edge_type_idx]---------epoch{}".format(epoch))
            print(minibatch.idx2edge_type[minibatch.current_edge_type_idx])
       
                
            print("Epoch:", "%04d" % (epoch + 1), "Iter:", "%04d" % (itr + 1), "Edge:", "%04d" % batch_edge_type,
                  "train_loss=", "{:.5f}".format(train_cost),
                  "val_roc=", "{:.5f}".format(val_auc), "val_auprc=", "{:.5f}".format(val_auprc),
                  "val_apk=", "{:.5f}".format(val_apk), "time=", "{:.5f}".format(time.time() - t))

        itr += 1

print("Optimization finished!")

